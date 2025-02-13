package xyz.sadiulhakim.stock;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
public class StockController {

    private final UserService userService;
    private final StockService stockService;

    public StockController(UserService userService, StockService stockService) {
        this.userService = userService;
        this.stockService = stockService;
    }

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final Map<String, List<SseEmitter>> symbolEmitters = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @GetMapping("/stock/create_page")
    String stockCreatePage(Model model) {
        model.addAttribute("dto", new Stock());
        return "stock/create_page";
    }

    @PostMapping("/stock/create")
    String stockCreatePage(@ModelAttribute Stock stock, RedirectAttributes model) {

        boolean saved = stockService.save(stock);
        model.addFlashAttribute("saved", saved);

        return "redirect:/stock/create_page";
    }

    @GetMapping("/")
    String stocksPage(Model model, Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "stocks";
    }

    @GetMapping("/symbol/{symbol}")
    String symbolPage(@PathVariable String symbol, Model model) {
        model.addAttribute("symbol", symbol);
        return "candle";
    }

    @GetMapping("/stocks-stream")
    public SseEmitter getStocksStream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter)); // Remove emitter when client disconnects
        emitter.onError((ex) -> emitters.remove(emitter)); // Remove emitter on error

        return emitter;
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendTopTraders() {
        List<Stock> stocks = stockService.getAllStocks();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("stocks").data(stocks));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    @GetMapping("/stream/{symbol}")
    public SseEmitter subscribeToStock(@PathVariable String symbol) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // Set timeout
        symbolEmitters.computeIfAbsent(symbol, k -> new CopyOnWriteArrayList<>()).add(emitter);

        // Remove emitter when it completes or errors out
        emitter.onCompletion(() -> removeEmitter(symbol, emitter));
        emitter.onTimeout(() -> removeEmitter(symbol, emitter));
        emitter.onError(e -> removeEmitter(symbol, emitter));

        return emitter;
    }

    private void removeEmitter(String symbol, SseEmitter emitter) {
        List<SseEmitter> emitters = symbolEmitters.get(symbol);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                symbolEmitters.remove(symbol);
            }
        }
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendUpdateSeparately() {
        List<Stock> stocks = stockService.getAllStocks();

        for (Stock stock : stocks) {
            double open = stock.getPrice();
            double close = open + (random.nextDouble() - 0.5) * 1.5;
            double high = Math.max(open, close) + random.nextDouble();
            double low = Math.min(open, close) - random.nextDouble();

            String priceData = String.format(
                    "{\"symbol\": \"%s\", \"time\": %d, \"open\": %.2f, \"high\": %.2f, \"low\": %.2f, \"close\": %.2f}",
                    stock.getSymbol(), System.currentTimeMillis() / 1000, open, high, low, close
            );

            // Send updates only to subscribers of this symbol
            List<SseEmitter> emitters = symbolEmitters.getOrDefault(stock.getSymbol(), Collections.emptyList());
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("priceUpdate").data(priceData));
                } catch (IOException e) {
                    emitter.complete();
                }
            }
        }
    }
}

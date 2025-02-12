package xyz.sadiulhakim.stock;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Controller
public class StockController {

    private final UserService userService;
    private final StockService stockService;

    public StockController(UserService userService, StockService stockService) {
        this.userService = userService;
        this.stockService = stockService;
    }

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
}

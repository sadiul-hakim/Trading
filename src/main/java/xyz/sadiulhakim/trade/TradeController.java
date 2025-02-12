package xyz.sadiulhakim.trade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestParam Long userId, @RequestParam String symbol,
                                           @RequestParam int quantity) {
        String result = tradeService.buyStock(userId, symbol, quantity);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestParam Long userId, @RequestParam String symbol,
                                            @RequestParam int quantity) {
        String result = tradeService.sellStock(userId, symbol, quantity);
        return ResponseEntity.ok(result);
    }
}

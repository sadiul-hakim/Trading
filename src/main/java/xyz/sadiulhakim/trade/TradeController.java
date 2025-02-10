package xyz.sadiulhakim.trade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.sadiulhakim.user.UserRepository;

@RestController
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService tradeService, UserRepository userRepository) {
        this.tradeService = tradeService;
    }

    @PostMapping("/buy")
    @ResponseBody
    public ResponseEntity<String> buyStock(@RequestParam Long userId, @RequestParam Long stockId,
                                           @RequestParam int quantity) {
        String result = tradeService.buyStock(userId, stockId, quantity);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sell")
    @ResponseBody
    public ResponseEntity<String> sellStock(@RequestParam Long userId, @RequestParam Long stockId,
                                            @RequestParam int quantity) {
        String result = tradeService.sellStock(userId, stockId, quantity);
        return ResponseEntity.ok(result);
    }
}

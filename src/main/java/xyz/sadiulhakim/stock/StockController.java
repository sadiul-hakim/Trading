package xyz.sadiulhakim.stock;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Controller
public class StockController {

    private final UserService userService;

    public StockController(UserService userService) {
        this.userService = userService;
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

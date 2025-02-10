package xyz.sadiulhakim.stock;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Controller
public class StockController {

    private final UserService userService;

    public StockController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/stocks")
    String stocksPage(Model model) {

        User sadiulHakim = userService.findByUsername("Sadiul Hakim");
        model.addAttribute("user", sadiulHakim);
        return "stocks";
    }
}

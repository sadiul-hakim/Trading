package xyz.sadiulhakim.trade;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Controller
public class TradePageController {

    private final UserRepository userRepository;

    public TradePageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/trade_page")
    String tradePage(Model model) {

        User hakim = userRepository.findByUsername("Sadiul Hakim").orElse(new User());
        model.addAttribute("user", hakim);
        return "trade";
    }
}

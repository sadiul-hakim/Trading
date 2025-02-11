package xyz.sadiulhakim.portfolio;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserService userService;

    public PortfolioController(PortfolioService portfolioService, UserService userService) {
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @GetMapping("/portfolio")
    String portfolioPage(Model model, Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("portfolios", portfolioService.findAllByUser(user));

        return "portfolio";
    }
}

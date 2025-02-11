package xyz.sadiulhakim.portfolio;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;

    public PortfolioController(PortfolioService portfolioService, UserRepository userRepository) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
    }

    @GetMapping("/portfolio")
    String portfolioPage(Model model, Authentication authentication) {

        User hakim = userRepository.findByUsername(authentication.getName())
                .orElse(new User());
        model.addAttribute("user", hakim);
        model.addAttribute("portfolios", portfolioService.findAll());

        return "portfolio";
    }
}

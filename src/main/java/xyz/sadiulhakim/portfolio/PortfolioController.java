package xyz.sadiulhakim.portfolio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Controller
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioController(PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/portfolio")
    String portfolioPage(Model model) {

        User hakim = userRepository.findByUsername("Sadiul Hakim").orElse(new User());
        model.addAttribute("user", hakim);
        model.addAttribute("portfolios", portfolioRepository.findAll());

        return "portfolio";
    }
}

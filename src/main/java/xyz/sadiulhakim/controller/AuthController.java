package xyz.sadiulhakim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login_page")
    String loginPage() {
        return "login_page";
    }

    @GetMapping("/register_page")
    String registerPage(Model model) {

        model.addAttribute("dto", new User());
        return "register_page";
    }

    @PostMapping("/register")
    String register(@ModelAttribute User user, RedirectAttributes model) {
        userService.register(user);
        model.addFlashAttribute("registered", true);
        return "redirect:/login_page";
    }
}

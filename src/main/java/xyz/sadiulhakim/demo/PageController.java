package xyz.sadiulhakim.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class PageController {

    @GetMapping("/demo")
    String demoPage() {
        return "demo";
    }
}

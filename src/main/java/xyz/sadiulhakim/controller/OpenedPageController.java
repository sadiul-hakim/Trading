package xyz.sadiulhakim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OpenedPageController {

    @GetMapping("/notice")
    String noticePage() {
        return "notice";
    }
}

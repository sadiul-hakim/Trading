package xyz.sadiulhakim.stock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StockController {

    @GetMapping("/stocks")
    String stocksPage() {
        return "stocks";
    }
}

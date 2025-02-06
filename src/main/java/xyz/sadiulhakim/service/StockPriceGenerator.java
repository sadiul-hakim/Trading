package xyz.sadiulhakim.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xyz.sadiulhakim.config.StockSocketHandler;

import java.io.IOException;
import java.util.Random;

@Service
public class StockPriceGenerator {

    private final StockSocketHandler stockWebSocketHandler;
    private final Random random = new Random();
    private double lastClose = 175.0; // Initial price

    public StockPriceGenerator(StockSocketHandler stockWebSocketHandler) {
        this.stockWebSocketHandler = stockWebSocketHandler;
    }

    @Scheduled(fixedRate = 1000) // Runs every 1 second
    public void generateStockPrice() throws IOException {
        double open = lastClose;
        double close = open + (random.nextDouble() - 0.5) * 5; // Change between -2.5 and +2.5
        double high = Math.max(open, close) + random.nextDouble() * 2;
        double low = Math.min(open, close) - random.nextDouble() * 2;

        // Ensure stock price stays within 150 - 200
        if (low < 150) low = 150;
        if (high > 200) high = 200;

        lastClose = close; // Update last close price

        String priceData = String.format(
                "{\"time\": %d, \"open\": %.2f, \"high\": %.2f, \"low\": %.2f, \"close\": %.2f}",
                System.currentTimeMillis() / 1000, open, high, low, close
        );

        stockWebSocketHandler.broadcastStockPrice(priceData);
    }
}

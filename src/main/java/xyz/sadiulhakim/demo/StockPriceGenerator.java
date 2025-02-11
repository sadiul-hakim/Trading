//package xyz.sadiulhakim.demo;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Random;
//
//@Service
//public class StockPriceGenerator {
//
//    private final StockSocketHandler stockWebSocketHandler;
//    private final Random random = new Random();
//    private double lastClose = 175.0; // Initial price
//
//    public StockPriceGenerator(StockSocketHandler stockWebSocketHandler) {
//        this.stockWebSocketHandler = stockWebSocketHandler;
//    }
//
//    @Scheduled(fixedRate = 1000) // Runs every 1 second
//    public void generateStockPrice() throws IOException {
//        double open = lastClose;
//        double close = lastClose + (random.nextDouble() - 0.5) * 1.5;
//        close = 0.7 * lastClose + 0.3 * close;
//        double high = Math.max(open, close) + random.nextDouble(); // Max +1
//        double low = Math.min(open, close) - random.nextDouble(); // Max -1
//
//        // Ensure stock price stays within 150 - 200
//        if (low < 150) low = 150;
//        if (high > 200) high = 200;
//
//        // Update lastClose for the next iteration
//        lastClose = close;
//
//        String priceData = String.format(
//                "{\"time\": %d, \"open\": %.2f, \"high\": %.2f, \"low\": %.2f, \"close\": %.2f}",
//                System.currentTimeMillis() / 1000, open, high, low, close
//        );
//
//        stockWebSocketHandler.broadcastStockPrice(priceData);
//    }
//}

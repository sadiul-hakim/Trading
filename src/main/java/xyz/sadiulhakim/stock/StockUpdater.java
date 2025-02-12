package xyz.sadiulhakim.stock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class StockUpdater {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockService stockService;
    private final Random random = new Random();

    @Value("${trading.stocks.broadcasting.channel:''}")
    private String stocksChannel;

    @Value("${trading.symbol.broadcasting.channel:''}")
    private String symbolChannel;

    public StockUpdater(SimpMessagingTemplate messagingTemplate, StockService stockService) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
    }

    @Scheduled(fixedRate = 1000)
    public void updateStocks() {
        stockService.updateStockPrices();
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendStockUpdates() {
        List<Stock> stocks = stockService.getAllStocks();
        messagingTemplate.convertAndSend(stocksChannel, stocks);
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendUpdateSeparately() {
        List<Stock> stocks = stockService.getAllStocks();

        for (Stock stock : stocks) {

            double open = stock.getPrice();
            double close = open + (random.nextDouble() - 0.5) * 1.5;
            double high = Math.max(open, close) + random.nextDouble();
            double low = Math.min(open, close) - random.nextDouble();

            String priceData = String.format(
                    "{\"symbol\": \"%s\", \"time\": %d, \"open\": %.2f, \"high\": %.2f, \"low\": %.2f, \"close\": %.2f}",
                    stock.getSymbol(), System.currentTimeMillis() / 1000, open, high, low, close
            );

            messagingTemplate.convertAndSend(symbolChannel + stock.getSymbol(), priceData);
        }
    }
}

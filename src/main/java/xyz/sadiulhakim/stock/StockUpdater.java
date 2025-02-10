package xyz.sadiulhakim.stock;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockUpdater {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockService stockService;

    public StockUpdater(SimpMessagingTemplate messagingTemplate, StockService stockService) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
    }

    @Scheduled(fixedRate = 2000)
    public void sendStockUpdates() {
        List<Stock> stocks = stockService.updateStockPrices();
        messagingTemplate.convertAndSend("/topic/stocks", stocks);
    }
}

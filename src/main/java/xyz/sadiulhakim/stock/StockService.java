package xyz.sadiulhakim.stock;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        Random random = new Random();

        for (Stock stock : stocks) {
            double change = (random.nextDouble() * 2 - 1) * 5; // -5% to +5% change
            stock.setPrice(stock.getPrice() * (1 + change / 100));
            stock.setChange(change);
            stockRepository.save(stock);
        }

        return stocks;
    }
}

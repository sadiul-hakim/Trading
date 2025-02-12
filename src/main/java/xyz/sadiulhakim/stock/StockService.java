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

    public boolean save(Stock stock) {
        try {
            stockRepository.save(stock);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock findBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol).orElse(new Stock());
    }

    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        Random random = new Random();

        for (Stock stock : stocks) {
            // -0.5% to +0.5% change
            double change = (random.nextDouble() - 0.5); // Generates a value between -0.5 and 0.5
            stock.setPrice(stock.getPrice() * (1 + change / 100)); // Adjust price by the change percentage
            stock.setChange(change); // Set the percentage change for the stock
            stockRepository.save(stock);
        }
    }
}

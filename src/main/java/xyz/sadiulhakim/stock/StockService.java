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

    public Stock findBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol).orElse(new Stock());
    }

    public void updateStockPrices() {
        List<Stock> stocks = stockRepository.findAll();
        Random random = new Random();

        for (Stock stock : stocks) {
            // -1% to +1% change
            double change = (random.nextDouble() * 2 - 1); // Generates a value between -1 and 1
            stock.setPrice(stock.getPrice() * (1 + change / 100)); // Adjust price by the change percentage
            stock.setChange(change); // Set the percentage change for the stock
            stockRepository.save(stock);
        }
    }
}

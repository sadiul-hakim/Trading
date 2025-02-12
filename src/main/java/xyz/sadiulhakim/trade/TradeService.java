package xyz.sadiulhakim.trade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.sadiulhakim.portfolio.Portfolio;
import xyz.sadiulhakim.portfolio.PortfolioService;
import xyz.sadiulhakim.stock.Stock;
import xyz.sadiulhakim.stock.StockRepository;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Service
public class TradeService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final PortfolioService portfolioService;

    public TradeService(StockRepository stockRepository, UserRepository userRepository,
                        PortfolioService portfolioService) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.portfolioService = portfolioService;
    }

    @Transactional
    public String buyStock(Long userId, String symbol, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findBySymbol(symbol).orElseThrow();

        double cost = stock.getPrice() * quantity;
        if (user.getBalance() < cost) {
            return "Insufficient funds!";
        }

        user.setBalance(user.getBalance() - cost);
        userRepository.save(user);

        Portfolio portfolio = portfolioService.findByUserAndStock(user, stock);

        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        portfolioService.save(portfolio);

        return "Stock purchased successfully!";
    }

    @Transactional
    public String sellStock(Long userId, String symbol, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findBySymbol(symbol).orElseThrow();
        Portfolio portfolio = portfolioService.findByUserAndStock(user, stock);
        if (portfolio.getId() == null) {
            throw new RuntimeException("You do not own this stock");
        }

        if (portfolio.getQuantity() < quantity) {
            return "Not enough shares to sell!";
        }

        double earnings = stock.getPrice() * quantity;
        user.setBalance(user.getBalance() + earnings);
        userRepository.save(user);

        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        if (portfolio.getQuantity() == 0) {
            portfolioService.delete(portfolio);
        } else {
            portfolioService.save(portfolio);
        }

        return "Stock sold successfully!";
    }
}

package xyz.sadiulhakim.trade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.sadiulhakim.portfolio.Portfolio;
import xyz.sadiulhakim.portfolio.PortfolioRepository;
import xyz.sadiulhakim.stock.Stock;
import xyz.sadiulhakim.stock.StockRepository;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

@Service
public class TradeService {

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    public TradeService(StockRepository stockRepository, UserRepository userRepository,
                        PortfolioRepository portfolioRepository) {
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Transactional
    public String buyStock(Long userId, Long stockId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();

        double cost = stock.getPrice() * quantity;
        if (user.getBalance() < cost) {
            return "Insufficient funds!";
        }

        user.setBalance(user.getBalance() - cost);
        userRepository.save(user);

        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
                .orElse(new Portfolio(null, user, stock, 0));

        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        portfolioRepository.save(portfolio);

        return "Stock purchased successfully!";
    }

    @Transactional
    public String sellStock(Long userId, Long stockId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Stock stock = stockRepository.findById(stockId).orElseThrow();
        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
                .orElseThrow(() -> new RuntimeException("You do not own this stock"));

        if (portfolio.getQuantity() < quantity) {
            return "Not enough shares to sell!";
        }

        double earnings = stock.getPrice() * quantity;
        user.setBalance(user.getBalance() + earnings);
        userRepository.save(user);

        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolioRepository.save(portfolio);
        }

        return "Stock sold successfully!";
    }
}

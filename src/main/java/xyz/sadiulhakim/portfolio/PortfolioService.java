package xyz.sadiulhakim.portfolio;

import org.springframework.stereotype.Service;
import xyz.sadiulhakim.stock.Stock;
import xyz.sadiulhakim.user.User;

import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public void save(Portfolio portfolio) {
        portfolioRepository.save(portfolio);
    }

    public List<Portfolio> findAll() {
        return portfolioRepository.findAll();
    }

    public Portfolio findByUserAndStock(User user, Stock stock) {
        return portfolioRepository.findByUserAndStock(user, stock)
                .orElse(new Portfolio(null, user, stock, 0));
    }

    public List<Portfolio> findAllByUser(User user) {
        return portfolioRepository.findAllByUser(user);
    }

    public void delete(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);
    }
}

package xyz.sadiulhakim.leader_board;

import org.springframework.stereotype.Service;
import xyz.sadiulhakim.portfolio.Portfolio;
import xyz.sadiulhakim.portfolio.PortfolioRepository;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserRepository;

import java.util.List;

@Service
public class LeaderboardService {

    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;

    public LeaderboardService(UserRepository userRepository, PortfolioRepository portfolioRepository) {
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public List<User> getTopTraders() {
        List<User> users = userRepository.findAll();
        users.sort((u1, u2) -> Double.compare(getUserNetWorth(u2), getUserNetWorth(u1)));
        return users;
    }

    public double getUserNetWorth(User user) {
        List<Portfolio> portfolios = portfolioRepository.findAllByUser(user);
        double totalStockValue = portfolios.stream()
                .mapToDouble(p -> p.getQuantity() * p.getStock().getPrice())
                .sum();
        return user.getBalance() + totalStockValue;
    }
}

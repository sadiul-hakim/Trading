package xyz.sadiulhakim.leader_board;

import org.springframework.stereotype.Service;
import xyz.sadiulhakim.portfolio.Portfolio;
import xyz.sadiulhakim.portfolio.PortfolioService;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

import java.util.List;

@Service
public class LeaderboardService {

    private final UserService userService;
    private final PortfolioService portfolioService;

    public LeaderboardService(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    public List<User> getTopTraders() {
        List<User> users = userService.findAll();
        users.sort((u1, u2) -> Double.compare(getUserNetWorth(u2), getUserNetWorth(u1)));
        return users;
    }

    public double getUserNetWorth(User user) {
        List<Portfolio> portfolios = portfolioService.findAllByUser(user);
        double totalStockValue = portfolios.stream()
                .mapToDouble(p -> p.getQuantity() * p.getStock().getPrice())
                .sum();
        return user.getBalance() + totalStockValue;
    }
}

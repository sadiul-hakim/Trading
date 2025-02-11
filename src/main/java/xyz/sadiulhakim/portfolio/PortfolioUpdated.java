package xyz.sadiulhakim.portfolio;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

import java.util.List;

@Component
public class PortfolioUpdated {

    private final PortfolioService portfolioService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public PortfolioUpdated(PortfolioService portfolioService, UserService userService,
                            SimpMessagingTemplate messagingTemplate) {
        this.portfolioService = portfolioService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 2500)
    public void sendPortfolioUpdate() {
        List<User> users = userService.findAll(); // Fetch all users from DB

        for (User user : users) {
            List<Portfolio> portfolios = portfolioService.findAllByUser(user);
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/portfolio", portfolios);
        }
    }
}

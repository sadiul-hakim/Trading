package xyz.sadiulhakim.portfolio;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import xyz.sadiulhakim.leader_board.LeaderboardService;
import xyz.sadiulhakim.user.User;
import xyz.sadiulhakim.user.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final UserService userService;
    private final LeaderboardService leaderboardService;

    public PortfolioController(PortfolioService portfolioService, UserService userService,
                               LeaderboardService leaderboardService) {
        this.portfolioService = portfolioService;
        this.userService = userService;
        this.leaderboardService = leaderboardService;
    }

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/portfolio")
    String portfolioPage(Model model, Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());
        double netWorth = leaderboardService.getUserNetWorth(user);

        model.addAttribute("user", user);
        model.addAttribute("netWorth", netWorth);
        model.addAttribute("portfolios", portfolioService.findAllByUser(user));

        return "portfolio";
    }

    @GetMapping("/portfolio-stream")
    public SseEmitter getPortfolioStream(@RequestParam String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(username, emitter);

        emitter.onCompletion(() -> emitters.remove(username)); // Remove emitter when client disconnects
        emitter.onError((ex) -> emitters.remove(username)); // Remove emitter on error

        return emitter;
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendPortfolioUpdate() {
        List<User> users = userService.findAll(); // Fetch all users from DB

        for (User user : users) {
            List<Portfolio> portfolios = portfolioService.findAllByUser(user);

            SseEmitter emitter = emitters.get(user.getUsername());
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("portfolioUpdate").data(portfolios));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        }
    }
}

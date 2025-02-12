package xyz.sadiulhakim.leader_board;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.sadiulhakim.user.UserDTO;

import java.util.List;

@Component
public class LeaderBoardUpdater {

    private final LeaderboardService leaderboardService;
    public final SimpMessagingTemplate messagingTemplate;

    public LeaderBoardUpdater(LeaderboardService leaderboardService, SimpMessagingTemplate messagingTemplate) {
        this.leaderboardService = leaderboardService;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendTopTraders() {

        List<UserDTO> topTraders = leaderboardService.getTopTraders();
        messagingTemplate.convertAndSend("/topic/top_traders", topTraders);
    }
}

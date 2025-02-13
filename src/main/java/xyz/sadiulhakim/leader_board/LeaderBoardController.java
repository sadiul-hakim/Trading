package xyz.sadiulhakim.leader_board;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import xyz.sadiulhakim.user.UserDTO;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/leader_board")
public class LeaderBoardController {

    private final LeaderboardService leaderboardService;

    public LeaderBoardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping
    String leaderBoardPage(Model model) {
        model.addAttribute("topTraders", leaderboardService.getTopTraders());
        return "leader_board";
    }

    @GetMapping("/top-traders-stream")
    public SseEmitter getTopTradersStream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter)); // Remove emitter when client disconnects
        emitter.onError((ex) -> emitters.remove(emitter)); // Remove emitter on error

        return emitter;
    }

    @Scheduled(fixedRate = 2000, scheduler = "scheduledTaskScheduler")
    public void sendTopTraders() {
        List<UserDTO> topTraders = leaderboardService.getTopTraders();

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("topTraders").data(topTraders));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }
}

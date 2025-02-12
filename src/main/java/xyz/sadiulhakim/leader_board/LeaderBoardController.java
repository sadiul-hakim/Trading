package xyz.sadiulhakim.leader_board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leader_board")
public class LeaderBoardController {

    private final LeaderboardService leaderboardService;

    public LeaderBoardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping
    String leaderBoardPage(Model model) {
        model.addAttribute("topTraders", leaderboardService.getTopTraders());
        return "leader_board";
    }
}

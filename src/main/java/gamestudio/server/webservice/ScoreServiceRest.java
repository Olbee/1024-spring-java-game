package gamestudio.server.webservice;

import gamestudio.entity.Score;
import gamestudio.service.score.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game){
        return scoreService.getTopScores(game);
    }

}

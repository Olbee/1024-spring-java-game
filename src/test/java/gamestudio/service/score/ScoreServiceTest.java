package gamestudio.service.score;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;

import gamestudio.entity.Score;

public class ScoreServiceTest {

    private final ScoreService scoreService = new ScoreServiceJDBC();

    @Test
    public void resetTest() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("1024").size());
    }

    @Test
    public void addScoreTest() {
        scoreService.reset();

        Date date = new Date();

        scoreService.addScore(new Score("1024", "tester", date, 123));

        List<Score> scores = scoreService.getTopScores("1024");

        assertEquals(1, scores.size());

        assertEquals("1024", scores.get(0).getGame());
        assertEquals("tester", scores.get(0).getPlayer());
        assertEquals(date, scores.get(0).getPlayed_on());
        assertEquals(123, scores.get(0).getPoints());
    }

    @Test
    public void getTopScoresTest() {
        scoreService.reset();

        Date date = new Date();

        scoreService.addScore(new Score("1024", "tester", date, 100));
        scoreService.addScore(new Score("1024", "anonymous", date, 231));
        scoreService.addScore(new Score("mines", "nobody", date, 400));
        scoreService.addScore(new Score("1024", "qwerty", date, 150));

        List<Score> scores = scoreService.getTopScores("1024");

        assertEquals(3, scores.size());

        assertEquals("1024", scores.get(0).getGame());
        assertEquals("anonymous", scores.get(0).getPlayer());
        assertEquals(date, scores.get(0).getPlayed_on());
        assertEquals(231, scores.get(0).getPoints());

        assertEquals("1024", scores.get(1).getGame());
        assertEquals("qwerty", scores.get(1).getPlayer());
        assertEquals(date, scores.get(1).getPlayed_on());
        assertEquals(150, scores.get(1).getPoints());

        assertEquals("1024", scores.get(2).getGame());
        assertEquals("tester", scores.get(2).getPlayer());
        assertEquals(date, scores.get(2).getPlayed_on());
        assertEquals(100, scores.get(2).getPoints());
    }

}

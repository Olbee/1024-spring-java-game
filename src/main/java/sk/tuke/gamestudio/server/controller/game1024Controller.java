package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.FieldState;
import sk.tuke.gamestudio.core.MoveDirection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.score.ScoreService;

import javax.print.attribute.standard.Media;
import java.awt.*;
import java.util.Date;

@Controller
@RequestMapping("/game1024")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class game1024Controller {

    @Autowired
    authenticationController authenticationController;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private Field field = new Field(4, 4, 1);

    @RequestMapping
    public String game1024(@RequestParam(required = false) String command, @RequestParam(required = false) Integer column, Model model) {
        if (field.getState() == FieldState.PLAYING) {
            processCommand(command);
        }
        if (column != null) {
            for (int i = 1; i <= 5; i++) {
                if (column == i)
                    ratingService.setRating(new Rating("1024", authenticationController.getLoggedUserName(), new Date(), i));
            }
        }

        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("scores", scoreService.getTopScores("1024"));
        model.addAttribute("comments", commentService.getComments("1024"));
        model.addAttribute("userRating", ratingService.getRating("1024", authenticationController.getLoggedUserName()));
        model.addAttribute("averageRating", ratingService.getAverageRating("1024"));

        return "game1024";
    }

    private void processCommand(String command) {
        if (command != null && field.getState() == FieldState.PLAYING) {
            switch (command) {
                case "up": {
                    field.moveTiles(MoveDirection.UP);
                    break;
                }
                case "down": {
                    field.moveTiles(MoveDirection.DOWN);
                    break;
                }
                case "left": {
                    field.moveTiles(MoveDirection.LEFT);
                    break;
                }
                case "right": {
                    field.moveTiles(MoveDirection.RIGHT);
                    break;
                }
            }
        }
    }

    @RequestMapping(value = "/field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String field(@RequestParam(required = false) String command) {
        processCommand(command);
        if (authenticationController.isUserLogged() && (field.getState() == FieldState.LOST || field.getState() == FieldState.WON)) {
            scoreService.addScore(new Score("1024", authenticationController.getLoggedUserName(), new Date(), field.getScore()));
        }
        return getHtmlField();
    }

    public int getScore() { return field.getScore(); }
    public boolean isRated() {
        return (ratingService.getRating("1024", authenticationController.getLoggedUserName())) != 0;
    }
    public boolean isFirstLevel() {
        return field.getLevel() == 1;
    }
    public boolean isSecondLevel() {
        return field.getLevel() == 2;
    }
    public boolean isPlayingState() { return field.getState() == FieldState.PLAYING; }
    public boolean isWonState() { return field.getState() == FieldState.WON; }
    public boolean isLostState() { return field.getState() == FieldState.LOST; }

    @RequestMapping(value = "/score", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String currentScore() {
        return Integer.toString(field.getScore());
    }

    @RequestMapping(value = "/state", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String currentState() {
        return field.getState().toString();
    }


    @RequestMapping("/addComment")
    public String addComment(String comment) {
        commentService.addComment(new Comment("1024", authenticationController.getLoggedUserName(), new Date(), comment));
        return "redirect:/game1024";
    }

    @RequestMapping("/new/1")
    public String newGameLevel1() {
        field = new Field(4, 4, 1);
        return "redirect:/game1024";
    }

    @RequestMapping("/new/2")
    public String newGameLevel2() {
        field = new Field(4, 4, 2);
        return "redirect:/game1024";
    }

    public String getHtmlRateColumn() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>\n");
        sb.append("<tr>\n");
        for (int column = 1; column <= 5; column++) {
            sb.append("<td>\n");
            sb.append("<a href='/game1024?column=" + column + "'>\n");
            sb.append("<img src='/images/1024/rate.png'>");
            sb.append("</a>\n");
            sb.append("</td>\n");
        }
        sb.append("</tr>\n");
        sb.append("</table>\n");

        return sb.toString();
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.append("<td>\n");
                sb.append("<img src='/images/1024/" + field.getTile(row, column).getValue() + ".png'>");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

}

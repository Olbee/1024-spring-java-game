package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.FieldState;
import sk.tuke.gamestudio.core.MoveDirection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/game1024")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class game1024Controller {

    @Autowired
    authenticationController authenticationController;

    @Autowired
    private ScoreService scoreService;
    private Field field = new Field(4, 4, 1);

    @RequestMapping
    public String game1024(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {
        if (field.getState() == FieldState.PLAYING) {
            if (row != null && column != null) {
                if (row == 4 && column == 5) field.moveTiles(MoveDirection.UP);
                else if (row == 5 && column == 4) field.moveTiles(MoveDirection.LEFT);
                else if (row == 5 && column == 6) field.moveTiles(MoveDirection.RIGHT);
                else if (row == 6 && column == 5) field.moveTiles(MoveDirection.DOWN);

                if (authenticationController.isUserLogged() && (field.getState() == FieldState.LOST || field.getState() == FieldState.WON)) {
                    scoreService.addScore(new Score("1024", authenticationController.getLoggedUserName(), new Date(), field.getScore()));
                }
            }
        }
        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("scores", scoreService.getTopScores("1024"));

        return "game1024";
    }

    public String getCurrentTime() {
        return new Date().toString();
    }

    public String getState() {
        return field.getState().toString();
    }

    public boolean isFirstLevel() {
        return field.getLevel() == 1;
    }

    public boolean isSecondLevel() {
        return field.getLevel() == 2;
    }

    @RequestMapping("/new/1")
    public String newGameLevel1() {
        field = new Field(4, 4, 1);
        return "game1024";
    }

    @RequestMapping("/new/2")
    public String newGameLevel2() {
        field = new Field(4, 4, 2);
        return "game1024";
    }

    public String getMoveButtons() {
        StringBuilder sb = new StringBuilder();


        sb.append("<table>\n");
        for (int row = 4; row < 7; row++) {
            sb.append("<tr>\n");
            for (int column = 4; column < 7; column++) {
                sb.append("<td>\n");
                sb.append("<a href='/game1024?row=" + row + "&column=" + column + "'>\n");
                sb.append("<img src='/images/1024/" + getImageName(row, column) + ".png'>");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

    public String getImageName(int row, int column) {
        if (row == 4 && column == 5) return "up";
        if (row == 5 && column == 4) return "left";
        if (row == 5 && column == 6) return "right";
        if (row == 6 && column == 5) return "down";
        return "0";
    }

    @RequestMapping(value = "/field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    private String getHtmlField() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.append("<td>\n");
                sb.append("<a href='/game1024?row=" + row + "&column=" + column + "'>\n");
                sb.append("<img src='/images/1024/" + field.getTile(row, column).getValue() + ".png'>");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

}

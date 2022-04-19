package sk.tuke.gamestudio.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import sk.tuke.gamestudio.core.Field;
import sk.tuke.gamestudio.core.MoveDirection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@Controller
@RequestMapping("/1024")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class game1024Controller {

    private Field field = new Field(4, 4, 2);

    @RequestMapping
    public String game1024(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        if (row != null && column != null) {
            if (row == 4 && column == 5) field.moveTiles(MoveDirection.UP);
            if (row == 5 && column == 4) field.moveTiles(MoveDirection.LEFT);
            if (row == 5 && column == 6) field.moveTiles(MoveDirection.RIGHT);
            if (row == 6 && column == 5) field.moveTiles(MoveDirection.DOWN);
        }
        return "game1024";
    }

    public String getCurrentTime() {
        return new Date().toString();
    }

    public String getState() {
        return field.getState().toString();
    }

    @RequestMapping("/new")
    public String newGame() {
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
                sb.append("<a href='/1024?row=" + row + "&column=" + column + "'>\n");
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

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                sb.append("<td>\n");
                sb.append("<a href='/1024?row=" + row + "&column=" + column + "'>\n");
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

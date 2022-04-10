package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Date;

@Entity

@NamedQuery(name = "Score.getTopScores", query = "SELECT s FROM Score s WHERE s.game =:game ORDER BY s.points DESC")
@NamedQuery(name = "Score.resetScores", query = "DELETE FROM Score")

public class Score implements Serializable {

    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private Date played_on;
    private int points;

    public Score() {

    }

    public Score(String game, String player, Date played_on, int points) {
        this.game = game;
        this.player = player;
        this.played_on = played_on;
        this.points = points;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Date getPlayed_on() {
        return played_on;
    }

    public void setPlayed_on(Date played_on) {
        this.played_on = played_on;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                ", game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", played_on=" + played_on +
                ", points=" + points +
                '}';
    }
}

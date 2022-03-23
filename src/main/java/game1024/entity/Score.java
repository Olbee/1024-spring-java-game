package game1024.entity;

import java.util.Date;

public class Score {

    private String game;
    private String player;
    private Date played_on;
    private int points;

    public Score(String game, String player, Date played_on, int points) {
        this.game = game;
        this.player = player;
        this.played_on = played_on;
        this.points = points;
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
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", played_on=" + played_on +
                ", points=" + points +
                '}';
    }

}

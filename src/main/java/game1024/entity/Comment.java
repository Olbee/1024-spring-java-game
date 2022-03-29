package sk.tuke.gamestudio.entity;

import java.util.Date;

public class Comment {

    private String game;
    private String player;
    private Date commented_on;
    private String comment;

    public Comment(String game, String player, Date commented_on, String comment) {
        this.game = game;
        this.player = player;
        this.commented_on = commented_on;
        this.comment = comment;
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

    public Date getCommented_on() {
        return commented_on;
    }

    public void setCommented_on(Date commented_on) {
        this.commented_on = commented_on;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", commented_on=" + commented_on +
                ", comment='" + comment + '\'' +
                '}';
    }

}

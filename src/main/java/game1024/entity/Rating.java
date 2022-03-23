package game1024.entity;

import java.util.Date;

public class Rating {

    private String game;
    private String player;
    private Date rated_on;
    private int rating;

    public Rating(String game, String player, Date rated_on, int rating) {
        this.game = game;
        this.player = player;
        this.rated_on = rated_on;
        this.rating = rating;
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

    public Date getRated_on() {
        return rated_on;
    }

    public void setRated_on(Date rated_on) {
        this.rated_on = rated_on;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rated_on=" + rated_on +
                ", rating=" + rating +
                '}';
    }

}

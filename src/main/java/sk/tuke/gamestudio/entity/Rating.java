package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Date;

@Entity

@NamedQuery(name = "Rating.checkIfUserExists", query = "SELECT r.player FROM Rating r WHERE r.game =: game AND r.player =: player")
@NamedQuery(name = "Rating.updateRating", query = "UPDATE Rating r SET r.rating =: rating, r.rated_on  =: rated_on WHERE" +
        " r.game =: game AND r.player =: player")
@NamedQuery(name = "Rating.getRating", query = "SELECT r.rating FROM Rating r WHERE r.game =: game AND r.player =: player")
@NamedQuery(name = "Rating.getAverageRating", query = "SELECT AVG(r.rating) FROM Rating r WHERE r.game =: game")
@NamedQuery(name = "Rating.resetRatings", query = "DELETE FROM Rating")

public class Rating implements Serializable {

    @Id
    @GeneratedValue
    private int ident;

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

    public Rating() {
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

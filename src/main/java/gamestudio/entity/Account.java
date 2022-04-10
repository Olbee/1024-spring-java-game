package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity

@NamedQuery(name = "Account.getAccountPassword", query = "SELECT a.password FROM Account a WHERE a.game =: game AND a.login =: login")
@NamedQuery(name = "Account.resetAccounts", query = "DELETE FROM Account")

public class Account implements Serializable {

    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String login;
    private String password;

    public Account() {

    }

    public Account(String game, String login, String password) {
        this.game = game;
        this.login = login;
        this.password = password;
    }

    public int getIdent() {
        return ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "ident=" + ident +
                ", game='" + game + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

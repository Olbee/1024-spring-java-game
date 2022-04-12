package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Objects;

@Entity

@NamedQuery(name = "Account.getAccountPassword", query = "SELECT a.password FROM Account a WHERE a.game =: game AND a.login =: login")
@NamedQuery(name = "Account.resetAccounts", query = "DELETE FROM Account")

public class Account implements Serializable {

    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String email;
    private String login;
    private String password;

    public Account() {

    }

    public Account(String game, String email, String login, String password) {
        this.game = game;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return ident == account.ident && Objects.equals(game, account.game) && Objects.equals(email, account.email) && Objects.equals(login, account.login) && Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ident, game, email, login, password);
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
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}

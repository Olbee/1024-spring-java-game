package sk.tuke.gamestudio.service.account;

import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.account.AccountException;
import sk.tuke.gamestudio.service.account.AccountService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceJDBC implements AccountService {

    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "crazzysaynss";
    public static final String DELETE_STATEMENT = "DELETE FROM account";
    public static final String INSERT_STATEMENT = "INSERT INTO score (game, email, login, password) VALUES (?, ?, ?, ?)";
    public static final String SELECT_LOGIN = "SELECT  login FROM account WHERE game = ? AND login ?";
    public static final String SELECT_PASSWORD = "SELECT game, password FROM account WHERE game = ? AND login = ?";

    @Override
    public boolean addAccount(Account account) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
        ) {
            statement.setString(1, account.getGame());
            statement.setString(2, account.getEmail());
            statement.setString(3, account.getLogin());
            statement.setString(4, account.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new AccountException("Problem inserting score", e);
        }
        return true;
    }

    @Override
    public boolean isLoginUsed(String game, String login) {
        //dont need this :>
        return false;
    }

    @Override
    public boolean isEmailUsed(String game, String email) {
        //dont need this :>
        return false;
    }

    @Override
    public String getAccountPassword(String game, String login) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(SELECT_PASSWORD)
        ) {
            statement.setString(1, game);
            statement.setString(2, login);
            try (ResultSet rs = statement.executeQuery()){
                return rs.getString(4);
            }
        } catch (SQLException e) {
            throw new AccountException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new AccountException("Problem deleting score", e);
        }
    }
}

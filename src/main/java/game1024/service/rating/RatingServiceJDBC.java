package sk.tuke.gamestudio.service.rating;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {

    private static String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    private static String JDBC_USER = "postgres";
    private static String JDBC_PASSWORD = "crazzysaynss";
    private static String INSERT_NEW_USER_STATEMENT = "INSERT INTO Rating(game, player, rated_on, rating) VALUES (?, ?, ?, ?)";
    private static String SELECT_ALL_STATEMENT = "SELECT game, player, rated_on, rating FROM rating WHERE game = ?";
    private static String DELETE_TABLE_STATEMENT = "DELETE FROM rating";
    private static String UPDATE_USER_STATEMENT = "UPDATE rating SET rated_on = ?, rating = ? WHERE game = ? AND player = ?";
    private static String CHECK_FOR_USER_STATEMENT = "SELECT game, player, rated_on, rating FROM rating WHERE game = ? AND player = ?";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statementCheckUser = connection.prepareStatement(CHECK_FOR_USER_STATEMENT);
             PreparedStatement statementUpdateUser = connection.prepareStatement(UPDATE_USER_STATEMENT);
             PreparedStatement statementAddUser = connection.prepareStatement(INSERT_NEW_USER_STATEMENT);
        ) {
            statementCheckUser.setString(1, rating.getGame());
            statementCheckUser.setString(2, rating.getPlayer());
            ResultSet rs = statementCheckUser.executeQuery();
            if (rs.next()) { //if user exists -> update info
                statementUpdateUser.setTimestamp(1, new Timestamp(rating.getRated_on().getTime()));
                statementUpdateUser.setInt(2, rating.getRating());
                statementUpdateUser.setString(3, rating.getGame());
                statementUpdateUser.setString(4, rating.getPlayer());
                statementUpdateUser.executeUpdate();
            }
            else { //else -> add new user
                statementAddUser.setString(1, rating.getGame());
                statementAddUser.setString(2, rating.getPlayer());
                statementAddUser.setTimestamp(3, new Timestamp(rating.getRated_on().getTime()));
                statementAddUser.setInt(4, rating.getRating());
                statementAddUser.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STATEMENT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()){
                List<Rating> ratings = new ArrayList<Rating>();
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getInt(4)));
                }
                int averageRating = 0;
                for (int i = 0; i < ratings.size(); i++) {
                    averageRating += ratings.get(i).getRating();
                }
                if (ratings.size() == 0) {
                    return -1; //trying to get rating from empty table, -1 as an value of error
                }
                averageRating /= ratings.size();
                return averageRating;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT game, player, rated_on, rating FROM rating WHERE game = ? AND player = ?");
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()){
                List<Rating> ratings = new ArrayList<Rating>();
                int playerRating = 0;
                while (rs.next()) {
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getTimestamp(3),rs.getInt(4)));
                }
                for (int i = 0; i < ratings.size(); i++) {
                    playerRating += ratings.get(i).getRating();
                }
                return playerRating;
            }
        } catch (SQLException e) {
            throw new RatingException("problem getting rating", e);
        }
    }
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_TABLE_STATEMENT);
        } catch (SQLException e) {
            throw new RatingException("Problem resetting", e);
        }
    }

}

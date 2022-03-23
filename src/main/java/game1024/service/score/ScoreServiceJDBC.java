package game1024.service.score;

import game1024.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService {

    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "crazzysaynss";
    public static final String DELETE_STATEMENT = "DELETE FROM score";
    public static final String INSERT_STATEMENT = "INSERT INTO score (game, player, played_on, points) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "SELECT game, player, played_on, points FROM score WHERE game = ? ORDER BY points DESC LIMIT 10";

    @Override
    public void addScore(Score score) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
        ) {
            statement.setString(1, score.getGame());
            statement.setString(2, score.getPlayer());
            statement.setTimestamp(3, new Timestamp(score.getPlayed_on().getTime()));
            statement.setInt(4, score.getPoints());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw   new ScoreException("Problem inserting score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT)
        ) {
            statement.setString(1, game);
            try(ResultSet rs = statement.executeQuery()) {
                List<Score> scores = new ArrayList<Score>();
                while (rs.next()) {
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getInt(4)));
                }
                return scores;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting score", e);
        }
    }

    @Override
    public void reset() {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting score", e);
        }
    }
}

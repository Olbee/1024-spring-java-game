package game1024.service.comment;

import game1024.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    public static final String JDBC_URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String JDBC_USER = "postgres";
    public static final String JDBC_PASSWORD = "crazzysaynss";
    public static final String DELETE_STATEMENT = "DELETE FROM comment";
    public static final String INSERT_STATEMENT = "INSERT INTO comment (game, player, commented_on, comment) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "SELECT game, player, commented_on, comment FROM comment WHERE game = ? ORDER BY commented_on DESC LIMIT 10";


    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
        ) {
            statement.setString(1, comment.getGame());
            statement.setString(2, comment.getPlayer());
            statement.setTimestamp(3, new Timestamp(comment.getCommented_on().getTime()));
            statement.setString(4, comment.getComment());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("problem adding comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT);
        ) {
            statement.setString(1, game);
            try(ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<Comment>();
                while (rs.next()) {
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getString(4)));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem getting Comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try(Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting score", e);
        }
    }

}

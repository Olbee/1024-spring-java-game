package gamestudio.service.comment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Date;

import gamestudio.entity.Comment;

public class CommentServiceTest {

    private final CommentService commentService = new CommentServiceJDBC();

    @Test
    public void resetTest() {
        commentService.reset();
        assertEquals(0, commentService.getComments("1024").size());
    }

    @Test
    public void getCommentsTest() {
        commentService.reset();

        Date date = new Date();

        commentService.addComment(new Comment("1024", "tester", date, "first"));
        commentService.addComment(new Comment("mines", "qwerty", date, "second"));
        commentService.addComment(new Comment("1024", "zeus", date, "aaaaaaa"));
        commentService.addComment(new Comment("1024", "simple", date, "thebestone"));

        List<Comment> comments = commentService.getComments("1024");

        assertEquals(3, comments.size());

        assertEquals("1024", comments.get(0).getGame());
        assertEquals("tester", comments.get(0).getPlayer());
        assertEquals(date, comments.get(0).getCommented_on());
        assertEquals("first", comments.get(0).getComment());

        assertEquals("1024", comments.get(1).getGame());
        assertEquals("zeus", comments.get(1).getPlayer());
        assertEquals(date, comments.get(1).getCommented_on());
        assertEquals("aaaaaaa", comments.get(1).getComment());

        assertEquals("1024", comments.get(2).getGame());
        assertEquals("simple", comments.get(2).getPlayer());
        assertEquals(date, comments.get(2).getCommented_on());
        assertEquals("thebestone", comments.get(2).getComment());
    }

    @Test
    public void addCommentTest() {
        commentService.reset();

        Date date = new Date();

        commentService.addComment(new Comment("1024", "user1", date, "new comment"));

        List<Comment> comments = commentService.getComments("1024");

        assertEquals(1, comments.size());

        assertEquals("1024", comments.get(0).getGame());
        assertEquals("user1", comments.get(0).getPlayer());
        assertEquals(date, comments.get(0).getCommented_on());
        assertEquals("new comment", comments.get(0).getComment());
    }

}

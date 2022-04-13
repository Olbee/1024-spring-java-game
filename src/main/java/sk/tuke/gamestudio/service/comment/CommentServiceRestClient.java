package sk.tuke.gamestudio.service.comment;

import sk.tuke.gamestudio.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class CommentServiceRestClient implements CommentService {

    private final String url = "http://localhost:8080/api/comment";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) throws CommentException {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return Arrays.asList(restTemplate.getForObject(url + "/" + game, Comment[].class));
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("Reset is not supported on the web interface.");
    }
}

package gamestudio.server;

import gamestudio.service.account.AccountService;
import gamestudio.service.account.AccountServiceJPA;
import gamestudio.service.comment.CommentService;
import gamestudio.service.comment.CommentServiceJPA;
import gamestudio.service.rating.RatingService;
import gamestudio.service.rating.RatingServiceJPA;
import gamestudio.service.score.ScoreService;
import gamestudio.service.score.ScoreServiceJPA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Configuration
@EntityScan(basePackages = "gamestudio.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceJPA();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

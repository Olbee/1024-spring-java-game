package sk.tuke.gamestudio;

import sk.tuke.gamestudio.consoleui.ConsoleUI;
import sk.tuke.gamestudio.service.account.AccountService;
import sk.tuke.gamestudio.service.account.AccountServiceJDBC;
import sk.tuke.gamestudio.service.account.AccountServiceJPA;
import sk.tuke.gamestudio.service.account.AccountServiceRestClient;
import sk.tuke.gamestudio.service.comment.CommentService;
import sk.tuke.gamestudio.service.comment.CommentServiceJPA;
import sk.tuke.gamestudio.service.comment.CommentServiceRestClient;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJDBC;
import sk.tuke.gamestudio.service.rating.RatingServiceJPA;
import sk.tuke.gamestudio.service.rating.RatingServiceRestClient;
import sk.tuke.gamestudio.service.score.ScoreService;
import sk.tuke.gamestudio.service.score.ScoreServiceJDBC;
import sk.tuke.gamestudio.service.score.ScoreServiceJPA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.service.score.ScoreServiceRestClient;

import java.io.IOException;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                        pattern = "sk.tuke.gamestudio.server.*"))

public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI consoleui) {
        return args -> consoleui.playGame();
    }

    @Bean
    public ConsoleUI consoleui() throws IOException {
        return new ConsoleUI();
    }

    @Bean
    public ScoreService scoreService() {
        //return new ScoreServiceJDBC();
        //return new ScoreServiceJPA();
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        //return new CommentServiceJDBC();
        //return new CommentServiceJPA();
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        //return new RatingServiceJDBC();
        //return new RatingServiceJPA();
        return new RatingServiceRestClient();
    }

    @Bean
    public AccountService accountService() {
        //return new AccountServiceJDBC();
        //return new AccountServiceJPA();
        return new AccountServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

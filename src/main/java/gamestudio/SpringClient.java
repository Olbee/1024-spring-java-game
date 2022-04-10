package gamestudio;

import gamestudio.consoleui.ConsoleUI;
import gamestudio.service.account.AccountService;
import gamestudio.service.account.AccountServiceJPA;
import gamestudio.service.account.AccountServiceRestClient;
import gamestudio.service.comment.CommentService;
import gamestudio.service.comment.CommentServiceJPA;
import gamestudio.service.comment.CommentServiceRestClient;
import gamestudio.service.rating.RatingService;
import gamestudio.service.rating.RatingServiceJPA;
import gamestudio.service.rating.RatingServiceRestClient;
import gamestudio.service.score.ScoreService;
import gamestudio.service.score.ScoreServiceJPA;
import gamestudio.service.score.ScoreServiceRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                        pattern = "gamestudio.server.*"))

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
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }

    @Bean
    public AccountService accountService() {
        return new AccountServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

package game1024.service.rating;

import game1024.entity.Rating;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    private final RatingService ratingService = new RatingServiceJDBC();

    @Test
    void resetTest() {
        ratingService.reset();
        assertEquals(-1, ratingService.getAverageRating("1024"));
    }

    @Test
    void setRatingTest() {
        ratingService.reset();

        Date date = new Date();

        ratingService.setRating(new Rating("1024", "user111", date, 3));

        assertEquals(3, ratingService.getRating("1024", "user111"));
    }

    @Test
    void getAverageRatingTest() {
        ratingService.reset();

        Date date = new Date();

        ratingService.setRating(new Rating("1024", "abc", date, 1));
        ratingService.setRating(new Rating("1024", "qwe", date, 4));
        ratingService.setRating(new Rating("mines", "ttt", date, 3));
        ratingService.setRating(new Rating("1024", "oop", date, 5));

        int averageRating = (1 + 4 + 5) / 3; //sum of user scores divided by user count
        assertEquals(averageRating, ratingService.getAverageRating("1024"));
    }

    @Test
    void getRatingTest() {
        ratingService.reset();

        Date date = new Date();

        ratingService.setRating(new Rating("1024", "tester", date, 5));

        assertEquals(5, ratingService.getRating("1024", "tester"));
    }
}

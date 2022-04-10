package gamestudio.service.rating;

import gamestudio.entity.Rating;
import gamestudio.service.comment.CommentException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;


    @Override
    public void setRating(Rating rating) throws RatingException {
        if (entityManager.contains(rating)) {
            entityManager.createQuery("UPDATE Rating r SET r.rating =: rating, r.rated_on  =: rated_on WHERE" +
                            " r.game =: game AND r.player =: player")
                    .setParameter("rating", rating.getRating())
                    .setParameter("rated_on", rating.getRated_on())
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer()).executeUpdate();
        }
        else entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Object result = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult();
        if (result == null) return 0;
        else if (result instanceof Double) {
            return ((Double) result).intValue();
        }
        else return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Object result = entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", player).getSingleResult();
        System.out.println(result.toString());
        if (result == null) return 0;
        else return ((Integer)result).intValue();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}

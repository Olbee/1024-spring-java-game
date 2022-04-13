package sk.tuke.gamestudio.service.rating;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        Object exists = null;
        try {
            exists = entityManager.createNamedQuery("Rating.checkIfUserExists")
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer()).getSingleResult();
        }
        catch(NoResultException e) {}

        if (exists == null) {
            entityManager.persist(rating);
        }
        else {
            entityManager.createNamedQuery("Rating.updateRating")
                    .setParameter("rating", rating.getRating())
                    .setParameter("rated_on", rating.getRated_on())
                    .setParameter("game", rating.getGame())
                    .setParameter("player", rating.getPlayer())
                    .executeUpdate();
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Object result = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult();
        if (result == null) return 0;
        else return ((Double) result).intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Object result = null;
        try {
            result = entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game).setParameter("player", player).getSingleResult();
        }
        catch(NoResultException e) {

        }
        if (result == null) return 0;
        else return ((Integer) result).intValue();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}

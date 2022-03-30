package game1024.service.score;

import game1024.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        entityManager.persist(score);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        return entityManager.createQuery("select s from Score s order by s.points desc")
                .setMaxResults(10).getResultList();
    }

    @Override
    public void reset() throws ScoreException {
        entityManager.createNativeQuery("delete from score");
    }
}

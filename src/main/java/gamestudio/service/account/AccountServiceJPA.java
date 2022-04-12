package gamestudio.service.account;

import gamestudio.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class AccountServiceJPA implements AccountService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addAccount(Account account) {
        entityManager.persist(account);
    }

    @Override
    public String getAccountPassword(String game, String email, String login) {
        try {
            String result = (String) entityManager.createNamedQuery("Account.getAccountPassword")
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Account.resetAccounts").executeUpdate();
    }
}

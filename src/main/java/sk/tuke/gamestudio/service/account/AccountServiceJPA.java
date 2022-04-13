package sk.tuke.gamestudio.service.account;

import sk.tuke.gamestudio.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class AccountServiceJPA implements AccountService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addAccount(Account account) throws AccountException {
        entityManager.persist(account);
    }

    @Override
    public String getAccountPassword(String game, String login) throws AccountException {
        Object result = null;
        try {
            result = entityManager.createNamedQuery("Account.getAccountPassword")
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        if (result == null) return null;
        else return (String) result;
    }

    @Override
    public void reset() throws AccountException {
        entityManager.createNamedQuery("Account.resetAccounts").executeUpdate();
    }
}

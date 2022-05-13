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
    public boolean addAccount(Account account) throws AccountException {
        Object exists = null;
        try {
            exists = entityManager.createNamedQuery("Account.checkIfAccountLoginExists")
                    .setParameter("game", account.getGame())
                    .setParameter("login", account.getLogin())
                    .setParameter("email", account.getEmail())
                    .getSingleResult();
        }
        catch(NoResultException e) {}

        if (exists == null) {
            entityManager.persist(account);
            return true;
        }
        return false;
    }

    @Override
    public boolean isLoginUsed(String game, String login) throws AccountException {
        Object used = null;
        try {
            used = entityManager.createNamedQuery("Account.isLoginUsed")
                    .setParameter("game", game)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {}
        if (used == null) return false;
        else return true;
    }

    @Override
    public boolean isEmailUsed(String game, String email) throws AccountException {
        Object used = null;
        try {
            used = entityManager.createNamedQuery("Account.isEmailUsed")
                    .setParameter("game", game)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {}
        if (used == null) return false;
        else return true;
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

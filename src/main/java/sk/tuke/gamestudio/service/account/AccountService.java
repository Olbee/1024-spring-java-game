package sk.tuke.gamestudio.service.account;

import sk.tuke.gamestudio.entity.Account;

public interface AccountService {
    boolean addAccount(Account account);
    boolean isLoginUsed(String game, String login);
    boolean isEmailUsed(String game, String email);
    String getAccountPassword(String game, String login);
    void reset();
}

package sk.tuke.gamestudio.service.account;

import sk.tuke.gamestudio.entity.Account;

public interface AccountService {
    void addAccount(Account account);
    String getAccountPassword(String game, String login);
    void reset();
}

package gamestudio.service.account;

import gamestudio.entity.Account;

public interface AccountService {
    void addAccount(Account account);
    String getAccountPassword(String game, String login);
    void reset();
}

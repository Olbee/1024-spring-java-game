package sk.tuke.gamestudio.service.account;

import sk.tuke.gamestudio.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NoResultException;

public class AccountServiceRestClient implements AccountService {


    private final String url = "http://localhost:8080/api/account";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean addAccount(Account account) throws AccountException {
        restTemplate.postForEntity(url, account, Account.class);
        return true;
    }

    @Override
    public boolean isLoginUsed(String game, String login) {
        //i dont need this :>
        return true;
    }

    @Override
    public boolean isEmailUsed(String game, String email) {
        //i dont need this :>
        return true;
    }

    @Override
    public String getAccountPassword(String game, String login) throws AccountException {
        return restTemplate.getForObject(url + "/" + game + "/" + login, String.class);
    }

    @Override
    public void reset() throws AccountException {
        throw new UnsupportedOperationException("Reset is not supported on the web interface.");
    }
}

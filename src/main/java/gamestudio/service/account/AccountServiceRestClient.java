package gamestudio.service.account;

import gamestudio.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class AccountServiceRestClient implements AccountService {


    private String url = "http://localhost:8080/api/account";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addAccount(Account account) {
        restTemplate.postForEntity(url, account, Account.class);
    }

    @Override
    public String getAccountPassword(String game, String email, String login) {
        return restTemplate.getForObject(url + "/" + game + "/" + email + "/" + login, Account.class).getPassword();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Reset is not supported on the web interface.");
    }
}

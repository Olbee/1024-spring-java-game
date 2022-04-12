package gamestudio.server.webservice;

import gamestudio.entity.Account;
import gamestudio.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountServiceRest {

    @Autowired
    AccountService accountService;

    @PostMapping
    public void addAccount(@RequestBody Account account) {
        accountService.addAccount(account);
    }

    @RequestMapping("/{game}/{login}")
    public String getAccountPassword(@PathVariable String game, @PathVariable String email, @PathVariable String login) {
        return accountService.getAccountPassword(game, email, login);
    }
}

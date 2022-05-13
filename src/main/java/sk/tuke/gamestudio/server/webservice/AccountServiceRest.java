package sk.tuke.gamestudio.server.webservice;

import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.service.account.AccountService;
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
    public String getAccountPassword(@PathVariable String game, @PathVariable String login) {
        return accountService.getAccountPassword(game, login);
    }

    @RequestMapping("/{game}/loginUsed/{login}")
    public boolean isLoginUsed(@PathVariable String game, @PathVariable String login) {
        return accountService.isLoginUsed(game, login);
    }

    @RequestMapping("/{game}/emailUsed/{email}")
    public boolean isEmailUsed(@PathVariable String game, @PathVariable String email) {
        return accountService.isEmailUsed(game, email);
    }

}

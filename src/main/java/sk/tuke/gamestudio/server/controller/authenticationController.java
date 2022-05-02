package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.service.account.AccountService;

import java.util.Objects;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class authenticationController {

    @Autowired
    AccountService accountService;

    private String loggedUserName;

    @RequestMapping("/register")
    public String getRegisterPage() {
        return "register_page_1024";
    }

    @RequestMapping("/register/register")
    public String login(String email, String login, String password) {
        if (accountService.addAccount(new Account("1024", email, login, password))) {
            return "redirect:/login";
        }
        return "redirect:/register";
    }

    @RequestMapping("/login")
    public String getLoginPage() {
        return "login_page_1024";
    }

    @RequestMapping("/login/login")
    public String login(String login, String password) {
        if (Objects.equals(accountService.getAccountPassword("1024", login), password)) {
            loggedUserName = login;
            return "redirect:/game1024";
        }
        else {
            return "redirect:/login";
        }
    }

    @RequestMapping("/login/logout")
    public String logout() {
        loggedUserName = null;
        return "redirect:/";
    }

    public String getLoggedUserName() {
        return loggedUserName;
    }

    public boolean isUserLogged() {
        return loggedUserName != null;
    }
}

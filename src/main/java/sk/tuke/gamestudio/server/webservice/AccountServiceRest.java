package sk.tuke.gamestudio.server.webservice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Api(produces = "application/json", value = "Operations pertaining to manager accounts in the application")
public class AccountServiceRest {

    @Autowired
    AccountService accountService;

    @PostMapping()
    @ApiOperation(value = "Create a new account", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created a new account"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        accountService.addAccount(account);
        return new ResponseEntity<Account>(account, HttpStatus.OK);
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

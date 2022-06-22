package fh.server.controller;

import fh.server.entity.Account;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dao.LoginDAO;
import fh.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/check-login/{identifier}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean checkLogin(
            @PathVariable("identifier") String identifier
    ) {
        return accountService.existsByIdentifier(identifier);
    }

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AccountDTO createAccount(
            @RequestBody LoginDAO loginBlueprint
    ) {
        Account account = accountService.create(loginBlueprint);
        return new AccountDTO(account, account);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO login(
            @RequestBody LoginDAO login
    ) throws InterruptedException {
        Account account = accountService.login(login);
        return new AccountDTO(account, account);
    }

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO getAccount(
            @RequestHeader("Authorization") String token
    ) {
        Account account = accountService.fetchByToken(token);
        return new AccountDTO(account, account);
    }

    @PostMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO addLogin(
            @RequestHeader("Authorization") String token,
            @RequestBody LoginDAO blueprint
    ) {
        Account account = accountService.fetchByToken(token);
        account = accountService.addLogin(blueprint, account);
        return new AccountDTO(account, account);
    }

    @DeleteMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO removeLogin(
            @RequestHeader("Authorization") String token,
            @RequestBody String loginId
    ) {
        Account account = accountService.fetchByToken(token);
        account = accountService.removeLogin(loginId, account);
        return new AccountDTO(account, account);
    }

}

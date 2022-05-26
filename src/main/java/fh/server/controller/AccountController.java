package fh.server.controller;

import fh.server.entity.Account;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dao.LoginDAO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private final AccountService accountService;

    AccountController(
            AccountService accountService
    ) {
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
        return DTOMapper.INSTANCE.map(account); // no pruning needed; client is owner
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO login(
            @RequestBody LoginDAO login
    ) throws InterruptedException {
        Account account = accountService.login(login);
        return DTOMapper.INSTANCE.map(account); // no pruning needed; client is owner
    }

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO getAccount(
            @RequestHeader("Authorization") String token
    ) {
        Account principal = accountService.fetchByToken(token);
        return DTOMapper.INSTANCE.map(principal); // no pruning needed; client is owner
    }

    @PostMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO addLogin(
            @RequestHeader("Authorization") String token,
            @RequestBody LoginDAO blueprint
    ) {
        Account principal = accountService.fetchByToken(token);
        principal = accountService.addLogin(blueprint, principal);
        return DTOMapper.INSTANCE.map(principal); // no pruning needed; client is owner
    }

    @DeleteMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO removeLogin(
            @RequestHeader("Authorization") String token,
            @RequestBody String loginId
    ) {
        Account principal = accountService.fetchByToken(token);
        principal = accountService.removeLogin(loginId, principal);
        return DTOMapper.INSTANCE.map(principal); // no pruning needed; client is owner
    }

}

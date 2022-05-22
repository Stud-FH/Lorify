package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.login.Login;
import fh.server.entity.login.TokenLogin;
import fh.server.rest.dto.AccountDTO;
import fh.server.rest.dto.LoginBlueprint;
import fh.server.rest.dto.LoginDTO;
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

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LoginDTO createAccount(
            @RequestBody LoginBlueprint loginBlueprint
    ) {
        TokenLogin tokenLogin = accountService.create(loginBlueprint);
        return DTOMapper.INSTANCE.map(tokenLogin); // no pruning needed
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO login(
            @RequestBody LoginBlueprint login
    ) {
        Account account = accountService.login(login);
        return DTOMapper.INSTANCE.map(account); // no pruning needed
    }

    @GetMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AccountDTO getAccount(
            @RequestHeader("Authorization") String token
    ) {
        Account principal = accountService.fetchByToken(token);
        return DTOMapper.INSTANCE.map(principal);
    }

    @PostMapping("/account/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LoginDTO addLogin(
            @RequestHeader("Authorization") String token,
            @RequestBody LoginBlueprint blueprint
    ) {
        Account principal = accountService.fetchByToken(token);
        Login login = accountService.addLogin(blueprint, principal);
        return DTOMapper.INSTANCE.map(login); // no pruning needed
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
        return DTOMapper.INSTANCE.map(principal); // no pruning needed
    }

//    @PostMapping("/account/attribute")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public AccountDTO putAttribute(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AttributeBlueprint blueprint
//    ) {
//        Account principal = accountService.fetchByToken(token);
//        principal = accountService.putAttribute(blueprint, principal);
//        return DTOMapper.INSTANCE.map(principal); // no pruning needed
//    }
//
//    @DeleteMapping("/account/attribute")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public AccountDTO removeAttribute(
//            @RequestHeader("Authorization") String token,
//            @RequestBody AttributeBlueprint blueprint
//    ) {
//        Account principal = accountService.fetchByToken(token);
//        principal = accountService.removeAttribute(blueprint, principal);
//        return DTOMapper.INSTANCE.map(principal); // no pruning needed
//    }


}

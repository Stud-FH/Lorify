package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Scope;
import fh.server.rest.dao.AliasDAO;
import fh.server.rest.dto.AliasDTO;
import fh.server.service.AliasService;
import fh.server.service.AuthenticationService;
import fh.server.service.ScopeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AliasController {

    private final ScopeService scopeService;
    private final AliasService aliasService;
    private final AuthenticationService authenticationService;

    public AliasController(ScopeService scopeService, AliasService aliasService, AuthenticationService authenticationService) {
        this.scopeService = scopeService;
        this.aliasService = aliasService;
        this.authenticationService = authenticationService;
    }



    @PostMapping("/mount/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO mount(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.principalAsAccount(token);

        Alias created = aliasService.mount(parent, principal);
        return new AliasDTO(created, principal);
    }


    @PostMapping("/create-alias/{path}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AliasDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody AliasDAO dao
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.account(token);
        Alias created = aliasService.create(parent, dao, principal);
        return new AliasDTO(created, principal);
    }

    @PostMapping("/claim-alias/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO claim(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody AliasDAO dao
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.principalAsAccount(token);

        Alias claimed = aliasService.claim(parent, dao, principal);
        return new AliasDTO(claimed, principal);
    }


}

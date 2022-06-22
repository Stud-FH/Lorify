package fh.server.controller;

import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Scope;
import fh.server.rest.dao.ScopeDAO;
import fh.server.rest.dto.ScopeDTO;
import fh.server.service.AuthenticationService;
import fh.server.service.ScopeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScopeController {

    private final ScopeService scopeService;
    private final AuthenticationService authenticationService;

    public ScopeController(ScopeService scopeService, AuthenticationService authenticationService) {
        this.scopeService = scopeService;
        this.authenticationService = authenticationService;
    }




    /**
     * creates a site
     * @param token valid principal token
     * @param data must contain:
     *                - name(unique)
     *                - visibility
     *                - nameManagementPolicy
     *                - tagManagementPolicy
     *                - attributeManagementPolicy
     */
    @PostMapping("/site")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ScopeDTO site(
            @RequestHeader("Authorization") String token,
            @RequestBody ScopeDAO data
    ) {
        Account principal = authenticationService.account(token);
        Scope created = scopeService.site(data, principal);
        return new ScopeDTO(created, principal);
    }


    @PostMapping("/create-scope/{path}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ScopeDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody ScopeDAO dao
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.account(token);
        Scope created = scopeService.create(parent, dao, principal);
        return new ScopeDTO(created, principal);
    }


    @PutMapping("/update-scope/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScopeDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody ScopeDAO dao
    ) {
        Scope victim = scopeService.fetchScope(path);
        Principal principal = authenticationService.principal(token);
        Scope updated = scopeService.update(victim, dao, principal);
        return new ScopeDTO(updated, principal);
    }



}

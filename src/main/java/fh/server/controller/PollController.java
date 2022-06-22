package fh.server.controller;

import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Poll;
import fh.server.entity.Scope;
import fh.server.rest.dao.PollDAO;
import fh.server.rest.dto.PollDTO;
import fh.server.service.AuthenticationService;
import fh.server.service.PollService;
import fh.server.service.ScopeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PollController {

    private final ScopeService scopeService;
    private final PollService pollService;
    private final AuthenticationService authenticationService;

    public PollController(ScopeService scopeService, PollService pollService, AuthenticationService authenticationService) {
        this.scopeService = scopeService;
        this.pollService = pollService;
        this.authenticationService = authenticationService;
    }



    @PostMapping("/create-poll/{path}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PollDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody PollDAO dao
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.account(token);
        Poll created = pollService.create(parent, dao, principal);
        return new PollDTO(created, principal);
    }


    @PutMapping("/update-poll/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PollDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody PollDAO dao
    ) {
        Poll victim = pollService.fetchPoll(path);
        Principal principal = authenticationService.principal(token);
        Poll updated = pollService.update(victim, dao, principal);
        return new PollDTO(updated, principal);
    }



}

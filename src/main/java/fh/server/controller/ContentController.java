package fh.server.controller;

import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Content;
import fh.server.entity.Scope;
import fh.server.rest.dao.ContentDAO;
import fh.server.rest.dto.ContentDTO;
import fh.server.service.AuthenticationService;
import fh.server.service.ContentService;
import fh.server.service.ScopeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContentController {

    private final ScopeService scopeService;
    private final ContentService contentService;
    private final AuthenticationService authenticationService;

    public ContentController(ScopeService scopeService, ContentService contentService, AuthenticationService authenticationService) {
        this.scopeService = scopeService;
        this.contentService = contentService;
        this.authenticationService = authenticationService;
    }




    @PostMapping("/create-content/{path}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ContentDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody ContentDAO dao
    ) {
        Scope parent = scopeService.fetchScope(path);
        Account principal = authenticationService.account(token);
        Content created = contentService.create(parent, dao, principal);
        return new ContentDTO(created, principal);
    }


    @PutMapping("/update-content/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ContentDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path,
            @RequestBody ContentDAO dao
    ) {
        Content victim = contentService.fetchContent(path);
        Principal principal = authenticationService.principal(token);
        Content updated = contentService.update(victim, dao, principal);
        return new ContentDTO(updated, principal);
    }



}

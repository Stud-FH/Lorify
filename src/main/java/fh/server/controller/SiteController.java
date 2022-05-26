package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Entity;
import fh.server.entity.Site;
import fh.server.rest.dao.SiteDAO;
import fh.server.rest.dto.*;
import fh.server.rest.mapper.DTOMapper;
import fh.server.rest.mapper.PruningMapper;
import fh.server.service.AccountService;
import fh.server.service.AuthenticationService;
import fh.server.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SiteController {

    private final SiteService siteService;
    private final AuthenticationService authenticationService;
    private final AccountService accountService;

    SiteController(
            SiteService siteService,
            AuthenticationService authenticationService,
            AccountService accountService
    ) {
        this.siteService = siteService;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    @GetMapping("/check-site/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean checkSite(
            @PathVariable("siteName") String identifier
    ) {
        return siteService.existsByName(identifier);
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
    public SiteDTO create(
            @RequestHeader("Authorization") String token,
            @RequestBody SiteDAO data
    ) {
        Account principal = accountService.fetchByToken(token);
        Site created = siteService.createSite(data, principal);
        return DTOMapper.INSTANCE.map(created); // no pruning needed; principal is owner
    }


    @PutMapping("/modify-site/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SiteDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody SiteDAO data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Entity principal = authenticationService.principal(site, token);

        Site updated = siteService.operate(site, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(updated, principal));
    }


    @GetMapping("/site/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SiteDTO get(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Entity principal = authenticationService.principal(site, token);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(site, principal));
    }



}

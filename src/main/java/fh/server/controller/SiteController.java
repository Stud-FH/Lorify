package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Site;
import fh.server.rest.dto.*;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import fh.server.service.AliasService;
import fh.server.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class SiteController {

    private final SiteService siteService;
    private final AccountService accountService;

    SiteController(
            SiteService siteService,
            AccountService accountService
    ) {
        this.siteService = siteService;
        this.accountService = accountService;
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
            @RequestBody SiteBlueprint data
    ) {
        Account principal = accountService.fetchByToken(token);
        Site created = siteService.createSite(data, principal);
        return DTOMapper.INSTANCE.map(created); // no pruning needed; principal is owner
    }


    @PutMapping("/site/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SiteDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody SiteBlueprint data
    ) {
        Account principal = accountService.fetchByToken(token);
        Site site = siteService.fetchSiteByName(siteName);

        Site updated = siteService.update(site, data, principal);
        return DTOMapper.INSTANCE.map(updated); // no pruning needed; principal must be owner
    }



}

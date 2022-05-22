package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Site;
import fh.server.rest.dto.AliasBlueprint;
import fh.server.rest.dto.AliasDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.service.AccountService;
import fh.server.service.AliasService;
import fh.server.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AliasController {

    private final SiteService siteService;
    private final AliasService aliasService;
    private final AccountService accountService;

    AliasController(
            SiteService siteService,
            AliasService aliasService,
            AccountService accountService
    ) {
        this.siteService = siteService;
        this.aliasService = aliasService;
        this.accountService = accountService;
    }




    @GetMapping("/alias/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO get(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody AliasBlueprint data
    ) {
        return null; // TODO privacy?
    }


    @PostMapping("/alias/{siteName}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AliasDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody AliasBlueprint data
    ) {
        Account principal = accountService.fetchByToken(token);
        Site site = siteService.fetchSiteByName(siteName);

        Alias created = aliasService.create(site, data, principal);
        return DTOMapper.INSTANCE.map(created); // todo pruning?
    }


    @PostMapping("/claim-alias/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO claim(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody String data
    ) {
        Account principal = accountService.fetchByToken(token);
        Site site = siteService.fetchSiteByName(siteName);

        Alias claimed = aliasService.claim(site, data, principal);
        return DTOMapper.INSTANCE.map(claimed); // todo prune?
    }


    @PutMapping("/alias")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO update(
            @RequestHeader("Authorization") String token,
            @RequestBody AliasBlueprint data
    ) {
        Account principal = accountService.fetchByToken(token);

        Alias updated = aliasService.update(data, principal);
        return DTOMapper.INSTANCE.map(updated); // todo prune?
    }


}

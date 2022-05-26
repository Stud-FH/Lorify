package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Entity;
import fh.server.entity.Site;
import fh.server.rest.dao.AliasDAO;
import fh.server.rest.dto.AliasDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.rest.mapper.PruningMapper;
import fh.server.service.AccountService;
import fh.server.service.AliasService;
import fh.server.service.AuthenticationService;
import fh.server.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AliasController {

    private final SiteService siteService;
    private final AliasService aliasService;
    private final AuthenticationService authenticationService;

    AliasController(
            SiteService siteService,
            AliasService aliasService,
            AuthenticationService authenticationService
    ) {
        this.siteService = siteService;
        this.aliasService = aliasService;
        this.authenticationService = authenticationService;
    }




    @GetMapping("/alias/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO get(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody AliasDAO data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Entity principal = authenticationService.principal(site, token);
        Alias alias = aliasService.fetchByName(site, data.getName());
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(alias, principal));
    }


    @PostMapping("/create-alias/{siteName}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public AliasDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody AliasDAO data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Account principal = authenticationService.principalAsAccount(token);

        Alias created = aliasService.create(site, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(created, principal));
    }


    @PostMapping("/claim-alias/{siteName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO claim(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody String data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Account principal = authenticationService.principalAsAccount(token);

        Alias claimed = aliasService.claim(site, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(claimed, principal));
    }


    @PutMapping("/modify-alias/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AliasDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id,
            @RequestBody AliasDAO data
    ) {
        Alias alias = aliasService.fetchById(id);
        Account principal = authenticationService.principalAsAccount(token);

        Alias updated = aliasService.operate(alias, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(updated, principal));
    }


}

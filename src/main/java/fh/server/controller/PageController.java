package fh.server.controller;

import fh.server.entity.*;
import fh.server.rest.dao.PageDAO;
import fh.server.rest.dto.PageDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.rest.mapper.PruningMapper;
import fh.server.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PageController {

    private final SiteService siteService;
    private final PageService pageService;
    private final AuthenticationService authenticationService;

    PageController(
            SiteService siteService,
            PageService pageService,
            AuthenticationService authenticationService
    ) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create-page/{siteName}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PageDTO create(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @RequestBody PageDAO data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Account principal = authenticationService.principalAsAccount(token);
        Page created = pageService.createPage(site, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(created, principal));
    }


    @PutMapping("/modify-page/{siteName}/{pageName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @PathVariable("pageName") String pageName,
            @RequestBody PageDAO data
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Entity principal = authenticationService.principal(site, token);
        Page page = pageService.fetchPageByName(site, pageName);

        Page updated = pageService.update(page, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(updated, principal));
    }


    @GetMapping("/site/{siteName}/{pageName}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO get(
            @RequestHeader("Authorization") String token,
            @PathVariable("siteName") String siteName,
            @PathVariable("pageName") String pageName
    ) {
        Site site = siteService.fetchSiteByName(siteName);
        Page page = pageService.fetchPageByName(site, pageName);
        Entity principal = authenticationService.principal(site, token);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(page, principal));
    }



}

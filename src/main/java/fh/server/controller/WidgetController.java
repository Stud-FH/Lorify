package fh.server.controller;

import fh.server.entity.Account;
import fh.server.entity.Entity;
import fh.server.entity.Page;
import fh.server.entity.Site;
import fh.server.entity.widget.Widget;
import fh.server.rest.dao.PageDAO;
import fh.server.rest.dao.WidgetDAO;
import fh.server.rest.dto.PageDTO;
import fh.server.rest.dto.WidgetDTO;
import fh.server.rest.mapper.DTOMapper;
import fh.server.rest.mapper.PruningMapper;
import fh.server.service.AuthenticationService;
import fh.server.service.PageService;
import fh.server.service.SiteService;
import fh.server.service.WidgetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WidgetController {

    private final SiteService siteService;
    private final PageService pageService;
    private final WidgetService widgetService;
    private final AuthenticationService authenticationService;

    WidgetController(
            SiteService siteService,
            PageService pageService,
            WidgetService widgetService,
            AuthenticationService authenticationService
    ) {
        this.siteService = siteService;
        this.pageService = pageService;
        this.widgetService = widgetService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create-widget")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public WidgetDTO create(
            @RequestHeader("Authorization") String token,
            @RequestBody WidgetDAO data,
            @RequestParam("pageId") Optional<String> pageId,
            @RequestParam("position") Optional<String> position
    ) {
        Account principal = authenticationService.principalAsAccount(token);
        Widget created = widgetService.createWidget(data, principal);
        pageId.ifPresent(id -> {
            widgetService.putWidget(created, pageId.get(), position.orElse(null), principal);
        });
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(created, principal));
    }


    @PutMapping("/modify-widget/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WidgetDTO update(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id,
            @RequestBody WidgetDAO data
    ) {
        Account principal = authenticationService.principalAsAccount(token);
        Widget widget = widgetService.fetchWidgetById(id);

        Widget updated = widgetService.operate(widget, data, principal);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(updated, principal));
    }


    @GetMapping("/widget/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WidgetDTO get(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id
    ) {
        Account principal = authenticationService.principalAsAccount(token);
        Widget widget = widgetService.fetchWidgetById(id);
        return DTOMapper.INSTANCE.map(PruningMapper.INSTANCE.prune(widget, principal));
    }



}

package fh.server.service;

import fh.server.entity.*;

import fh.server.entity.widget.Widget;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dto.AliasBlueprint;
import fh.server.rest.dto.ArtifactBlueprint;
import fh.server.rest.dto.EntityBlueprint;
import fh.server.rest.dto.PageBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
public class PageService extends ArtifactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);



    protected final PageRepository pageRepository;
    protected final WidgetRepository widgetRepository;
    protected final SiteRepository siteRepository;

    public PageService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository,
            @Qualifier("pageRepository") PageRepository pageRepository,
            @Qualifier("widgetRepository") WidgetRepository widgetRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository

    ) {
        super(entityRepository, artifactRepository);
        this.pageRepository = pageRepository;
        this.widgetRepository = widgetRepository;
        this.siteRepository = siteRepository;
    }

    /**
     * @return  site, principal, page, artifact
     */
    public Context buildContext(Page page, Account principal) {
        Site site = siteRepository.getById(page.getSiteId());
        return Context.build()
                .principal(principal)
                .page(page)
                .artifact(page)
                .site(site)
                .dispatch();
    }

    /**
     * @return  site, principal
     */
    public Context buildContext(Site site, Account principal) {
        return Context.build()
                .principal(principal)
                .site(site)
                .dispatch();
    }



    public Page fetchPageById(String id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
    }

    public Page fetchPageByName(Site site, String name) {
        return pageRepository.findBySiteIdAndName(site.getId(), name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
    }

    public Alias fetchAlias(Site site, Account account) {
        Alias alias = site.getAlias(account);
        if (alias == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found");
        return alias;
    }

    public Page createPage(Site site, PageBlueprint blueprint, Account principal) {
        Context context = buildContext(site, principal);
        checkPageCreator(context);
        checkNotEmpty(blueprint.getName(), "name");
        checkPageNameUnique(blueprint.getName(), context);

        Page page = new Page();
        page.addOwner(principal);
        page.setSiteId(site.getId());
        page.setName(blueprint.getName());
        page.setCreatorGuardDescription(blueprint.getCreatorGuardDescription());
        if (blueprint.getWidgets() != null) {
            Page finalPage = page;
            for (String position : blueprint.getWidgets().keySet()) {
                widgetRepository.findById(blueprint.getWidgets().get(position)).ifPresent(w -> finalPage.putWidget(position, w));
            }
        }
        page.putAttributes(blueprint.getAttributes());
        page.addTags(blueprint.getTags());
        page.setComments(blueprint.getComments());
        page.setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());

        page = saveAndFlush(page);
        site.putPage(page.getId(), page);
        siteRepository.flush();
        LOGGER.info(String.format("page created. [%s=%s, site=%s, principal=%s]", typeLabel(), page, site, principal));
        return page;
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.verifyUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((PageService) service).verifyNameUpdate(((PageBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("creatorGuardDescription")) {
            ((PageService) service).verifyCreGuardUpdate(((PageBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("widget")) {
            ((PageService) service).verifyWidgetUpdate(((PageBlueprint) blueprint), context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.performUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((PageService) service).performNameUpdate(((PageBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("creatorGuardDescription")) {
            ((PageService) service).performCreGuardUpdate(((PageBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("widget")) {
            ((PageService) service).performWidgetUpdate(((PageBlueprint) blueprint), context);
        }
    }

    public void verifyNameUpdate(PageBlueprint blueprint, Context context) {
        checkNotNull(context.getPage());
        checkNotNull(context.getSite());
        checkOwner(context);
        checkNotEmpty(blueprint.getName(), "name");
        checkPageNameUnique(blueprint.getName(), context);
    }

    public void performNameUpdate(PageBlueprint blueprint, Context context) {
        String previous = context.getPage().getName();
        context.getPage().setName(blueprint.getName());
        pageRepository.flush();
        LOGGER.info(String.format("name updated: %s -> %s in %s", previous, blueprint.getName(), context));
    }

    public void verifyCreGuardUpdate(PageBlueprint blueprint, Context context) {
        checkNotNull(context.getPage());
        checkOwner(context);
    }

    public void performCreGuardUpdate(PageBlueprint blueprint, Context context) {
        String previous = context.getPage().getCreatorGuardDescription();
        context.getPage().setCreatorGuardDescription(blueprint.getCreatorGuardDescription());
        pageRepository.flush();
        LOGGER.info(String.format("creatorGuardDescription updated: %s -> %s in %s", previous, blueprint.getCreatorGuardDescription(), context));
    }

    protected void verifyWidgetUpdate(PageBlueprint blueprint, Context context) {
        checkNotNull(context.getPage());
        checkNotNull(context.getSite());
        checkPageEditor(context);
        checkNotNull(blueprint.getWidgetKeys());
        checkNotNull(blueprint.getWidgets());
        for (String key : blueprint.getWidgetKeys()) {
            checkWidgetExistence(blueprint.getWidgets().get(key));
        }
    }

    protected void performWidgetUpdate(PageBlueprint blueprint, Context context) {
        StringBuilder update = new StringBuilder();
        for (String key : blueprint.getWidgetKeys()) {
            if (key == null ||key.isEmpty()) {
                Widget previous = context.getPage().removeWidget(key);
                update.append(String.format("%s\"%s\" removed (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, previous));
            } else {
                Widget value = widgetRepository.findById(blueprint.getWidgets().get(key)).orElse(null);
                Widget previous = context.getPage().putWidget(key, value);
                update.append(String.format("%s\"%s\" -> \"%s\" (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, value, previous));
            }
        }
        update.append("}");
        pageRepository.flush();
        LOGGER.info(String.format("widgets updated: %s in %s", update, context));
    }

    private void checkWidgetExistence(String id) {
        if (id == null || id.isEmpty()) return;
        if (!widgetRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "widget not found: "+id);
    }

    private void checkPageCreator(Context context) {
        if (!context.getSite().getCreatorGuard().resolve(context))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    private void checkPageEditor(Context context) {
        if (!context.getPage().getCreatorGuard().resolve(context))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    private void checkPageNameUnique(String name, Context context) {
        if (context.getSite().getPages().values().stream().anyMatch(p -> p.getName().equals(name)))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name collision");
    }

    protected Page saveAndFlush(Page created) {
        super.saveAndFlush(created);
        return pageRepository.saveAndFlush(created);
    }

    protected String typeLabel() {
        return "page";
    }

}

package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Page;
import fh.server.entity.Site;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dto.EntityBlueprint;
import fh.server.rest.dto.SiteBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SiteService extends ArtifactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteService.class);



    protected final SiteRepository siteRepository;
    protected final PageRepository pageRepository;

    public SiteService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository,
            @Qualifier("pageRepository") PageRepository pageRepository

    ) {
        super(entityRepository, artifactRepository);
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }




    public Site fetchSiteById(String id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found"));
    }

    public Site fetchSiteByName(String name) {
        return siteRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found"));
    }

    /**
     * @return  site, artifact, entity, principal
     */
    public Context buildContext(Site site, Account principal) {
        return Context.build()
                .principal(principal)
                .site(site)
                .artifact(site)
                .entity(site)
                .dispatch();
    }

    public Site createSite(SiteBlueprint blueprint, Account principal) {
        checkSiteCreator(principal);
        checkNotEmpty(blueprint.getName(), "name");
        checkSiteNameUnique(blueprint.getName());
        checkNotNull(blueprint.getVisibility());
        checkNotNull(blueprint.getNameManagementPolicy());
        checkNotNull(blueprint.getTagManagementPolicy());
        checkNotNull(blueprint.getAttributeManagementPolicy());

        Site site = new Site();
        site.addOwner(principal);
        site.setName(blueprint.getName());
        site.setVisibility(blueprint.getVisibility());
        site.setNameManagementPolicy(blueprint.getNameManagementPolicy());
        site.setTagManagementPolicy(blueprint.getTagManagementPolicy());
        site.setAttributeManagementPolicy(blueprint.getAttributeManagementPolicy());
        site.putAttributes(blueprint.getAttributes());
        site.addTags(blueprint.getTags());
        site.setComments(blueprint.getComments());
        site.setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());

        site = saveAndFlush(site);
        LOGGER.info(String.format("site created. [%s=%s, principal=%s]", typeLabel(), site, principal));
        return site;
    }

    public Site update(Site site, SiteBlueprint blueprint, Account principal) {
        checkNotNull(blueprint);
        super.update(blueprint, buildContext(site, principal), this);
        return site;
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.verifyUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((SiteService) service).verifyNameUpdate(((SiteBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("creatorGuardDescription")) {
            ((SiteService) service).verifyCreGuardUpdate(((SiteBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("page")) {
            ((SiteService) service).verifyPageUpdate(((SiteBlueprint) blueprint), context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.performUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((SiteService) service).performNameUpdate(((SiteBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("creatorGuardDescription")) {
            ((SiteService) service).performCreGuardUpdate(((SiteBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("page")) {
            ((SiteService) service).performPageUpdate(((SiteBlueprint) blueprint), context);
        }
    }

    public void verifyNameUpdate(SiteBlueprint blueprint, Context context) {
        checkOwner(context);
        checkNotEmpty(blueprint.getName(), "name");
        checkSiteNameUnique(blueprint.getName());
    }

    public void performNameUpdate(SiteBlueprint blueprint, Context context) {
        String previous = context.getSite().getName();
        context.getSite().setName(blueprint.getName());
        siteRepository.flush();
        LOGGER.info(String.format("name updated: %s -> %s in %s", previous, blueprint.getName(), context));
    }

    public void verifyCreGuardUpdate(SiteBlueprint blueprint, Context context) {
        checkOwner(context);
    }

    public void performCreGuardUpdate(SiteBlueprint blueprint, Context context) {
        String previous = context.getSite().getCreatorGuardDescription();
        context.getSite().setCreatorGuardDescription(blueprint.getCreatorGuardDescription());
        siteRepository.flush();
        LOGGER.info(String.format("creatorGuardDescription updated: %s -> %s in %s", previous, blueprint.getCreatorGuardDescription(), context));
    }

    protected void verifyPageUpdate(SiteBlueprint blueprint, Context context) {
        checkSiteEditor(context);
        checkNotNull(blueprint.getPageKeys());
        checkNotNull(blueprint.getPages());
        for (String key : blueprint.getPageKeys()) {
            checkPageExistence(blueprint.getPages().get(key));
        }
    }

    protected void performPageUpdate(SiteBlueprint blueprint, Context context) {
        StringBuilder update = new StringBuilder();
        for (String key : blueprint.getPageKeys()) {
            if (key == null ||key.isEmpty()) {
                Page previous = context.getSite().removePage(key);
                update.append(String.format("%s\"%s\" removed (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, previous));
            } else {
                Page value = pageRepository.findById(blueprint.getPages().get(key)).orElse(null);
                Page previous = context.getSite().putPage(key, value);
                update.append(String.format("%s\"%s\" -> \"%s\" (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, value, previous));
            }
        }
        update.append("}");
        siteRepository.flush();
        LOGGER.info(String.format("pages updated: %s in %s", update, context));
    }

    private void checkSiteCreator(Account principal) {

        // TODO implement coupons / subscriptions

        if (!principal.hasAttribute("premium"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "premium plan required");
    }

    private void checkSiteNameUnique(String name) {
        if (siteRepository.existsByName(name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "site name collision");
    }

    private void checkPageExistence(String id) {
        if (id == null || id.isEmpty()) return;
        if (!pageRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found: "+id);
    }

    private void checkSiteEditor(Context context) {
        if (!context.getSite().getCreatorGuard().resolve(context))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    protected Site saveAndFlush(Site created) {
        super.saveAndFlush(created);
        return siteRepository.saveAndFlush(created);
    }

    protected String typeLabel() {
        return "site";
    }

}

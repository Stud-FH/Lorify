package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Entity;
import fh.server.entity.Page;
import fh.server.entity.Site;
import fh.server.helpers.Context;
import fh.server.helpers.Operation;
import fh.server.repository.*;
import fh.server.rest.dao.EntityDAO;
import fh.server.rest.dao.SiteDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SiteService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteService.class);



    protected final SiteRepository siteRepository;
    protected final PageRepository pageRepository;

    public SiteService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository,
            @Qualifier("pageRepository") PageRepository pageRepository

    ) {
        super(entityRepository);
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }




    public boolean existsByName(String name) {
        return siteRepository.existsByName(name);
    }

    public Site fetchSiteById(String id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found"));
    }

    public Site fetchSiteByName(String name) {
        return siteRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found"));
    }

    public Page fetchPageById(String id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
    }

    public Site createSite(SiteDAO dao, Account principal) {
        checkSiteCreator(principal);
        checkNotNull(dao);
        checkNotEmpty(dao.getName(), "name");
        checkSiteNameUnique(dao.getName());
        checkNotNull(dao.getVisibility());

        Site site = new Site();
        site.setOwner(principal);
        site.setName(dao.getName());
        site.setVisibility(dao.getVisibility());
        site.adapt(dao);

        site = saveAndFlush(site);
        LOGGER.info(String.format("site created. [%s=%s, principal=%s]", typeLabel(), site, principal));
        return site;
    }

    public Site operate(Site site, SiteDAO dao, Entity principal) {
        super.operate(dao, new Context(principal, site), this);
        return site;
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {
        SiteDAO siteDao = (SiteDAO) dao;

        switch (opKey) {
            case "name": {
                checkNotEmpty(siteDao.getName(), "name");
                checkSiteNameUnique(siteDao.getName());
                return context.operation(opKey, siteDao.getName(), context.victimAsSite().getName());
            }
            case "visibility": {
                checkNotNull(siteDao.getVisibility());
                return context.operation(opKey, siteDao.getVisibility(), context.victimAsSite().getVisibility());
            }
        }

        if (opKey.startsWith("Site.p:")) {
            String key = opKey.substring( "Site.p:".length());
            String pageId = siteDao.getPages().get(key);
            return context.operation(opKey, fetchPageById(pageId), context.victimAsSite().getPage(key));
        }
        return super.setup(opKey, dao, context);
    }

    protected void execute(Operation operation, EntityDAO dao) {
        SiteDAO siteDao = (SiteDAO) dao;
        String opKey = operation.getOperation();

        switch (opKey) {
            case "get": return;
            case "name": operation.victimAsSite().setName(siteDao.getName()); return;
            case "visibility": operation.victimAsSite().setVisibility(siteDao.getVisibility()); return;
        }
        if (opKey.startsWith("Site.p:")) {
            String key = opKey.substring( "Site.p:".length());
            operation.victimAsSite().putPage(key, operation.valueAsPage());
            return;
        }
        super.execute(operation, dao);
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

    protected Site saveAndFlush(Site created) {
        super.saveAndFlush(created);
        return siteRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        siteRepository.flush();
    }

    protected void deleteAndFlush(Site arg0) { // todo not sure whether this is the correct order
        siteRepository.delete(arg0);
        siteRepository.flush();
        super.deleteAndFlush(arg0);
    }

    protected String typeLabel() {
        return "site";
    }

}

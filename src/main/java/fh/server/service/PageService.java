package fh.server.service;

import fh.server.constant.SiteVisibility;
import fh.server.constant.TrustLevel;
import fh.server.entity.*;

import fh.server.entity.widget.Widget;
import fh.server.helpers.Context;
import fh.server.helpers.Operation;
import fh.server.repository.*;
import fh.server.rest.dao.EntityDAO;
import fh.server.rest.dao.PageDAO;
import fh.server.rest.dao.SiteDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PageService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);



    protected final PageRepository pageRepository;
    protected final WidgetRepository widgetRepository;
    protected final SiteRepository siteRepository;

    public PageService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("pageRepository") PageRepository pageRepository,
            @Qualifier("widgetRepository") WidgetRepository widgetRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository

    ) {
        super(entityRepository);
        this.pageRepository = pageRepository;
        this.widgetRepository = widgetRepository;
        this.siteRepository = siteRepository;
    }



    public Page fetchPageById(String id) {
        return pageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
    }

    public Widget fetchWidgetById(String id) {
        return widgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "widget not found"));
    }

    public Page fetchPageByName(Site site, String name) {
        return pageRepository.findByParentIdAndName(site.getId(), name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
    }

    public Alias fetchAlias(Site site, Account account) {
        Alias alias = site.getAlias(account);
        if (alias == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found");
        return alias;
    }

    public Page createPage(Site site, PageDAO dao, Account principal) {
        Page page = new Page();
        page.adapt(dao);
        page.setOwner(principal);
        page.setParentId(site.getId());
        page.setName(dao.getName());
        if (dao.getWidgets() != null) {
            Page finalPage = page;
            for (String position : dao.getWidgets().keySet()) {
                widgetRepository.findById(dao.getWidgets().get(position)).ifPresent(w -> finalPage.putWidget(position, w));
            }
        }

        Operation operation = new Context(principal, site).operation("create-page", page, null);
        checkNotEmpty(dao.getName(), "name");
        checkPageNameUnique(dao.getName(), operation);
        verifyOperation(operation);

        page = saveAndFlush(page);
        int index = 0;
        while (site.hasPage(""+index)) index++;
        site.putPage(""+index, page);
        siteRepository.flush();
        LOGGER.info(String.format("page created. [%s=%s, site=%s, principal=%s]", typeLabel(), page, site, principal));
        return page;
    }

    public Page update(Page page, PageDAO dao, Entity principal) {
        super.operate(dao, new Context(principal, page), this);
        return page;
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {
        PageDAO pageDao = (PageDAO) dao;

        if ("name".equals(opKey)) {
            checkNotEmpty(pageDao.getName(), "name");
            checkPageNameUnique(pageDao.getName(), context);
            return context.operation(opKey, pageDao.getName(), context.victimAsPage().getName());
        }

        if (opKey.startsWith("Page.w:")) {
            String key = opKey.substring( "Page.w:".length());
            String widgetId = pageDao.getWidgets().get(key);
            return context.operation(opKey, fetchWidgetById(widgetId), context.victimAsPage().getWidget(key));
        }
        return super.setup(opKey, dao, context);
    }

    protected void execute(Operation operation, EntityDAO dao) {
        PageDAO pageDao = (PageDAO) dao;
        String opKey = operation.getOperation();

        if ("name".equals(opKey)) {
            operation.victimAsPage().setName(pageDao.getName());
        }

        if (opKey.startsWith("Page.w:")) {
            String key = opKey.substring( "Page.w:".length());
            operation.victimAsPage().putWidget(key, operation.valueAsWidget());
            return;
        }
        super.execute(operation, dao);
    }

    private void checkPageNameUnique(String name, Context context) {
        if (context.victimAsSite().getPages().values().stream().anyMatch(p -> p.getName().equals(name)))
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

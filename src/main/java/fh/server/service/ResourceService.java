package fh.server.service;

import fh.server.constant.Permission;
import fh.server.constant.SpringContext;
import fh.server.context.Principal;
import fh.server.entity.*;
import fh.server.helpers.Paths;
import fh.server.repository.*;
import fh.server.rest.dao.ResourceDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ResourceService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);



    protected final ResourceRepository resourceRepository;
    protected final DataRepository dataRepository;

    public ResourceService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository

    ) {
        super(entityRepository);
        this.resourceRepository = resourceRepository;
        this.dataRepository = dataRepository;
    }



    public boolean existsByPath(String path) {
        return resourceRepository.existsByPath(path);
    }

    public Resource fetchResource(String path) {
        return resourceRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "resource not found"));
    }

    public Resource create(ResourceDAO dao, Principal principal, Permission permissionRequired, Resource template) {
        init(dao);
        principal.requireAccount();
        principal.requirePermission(dao.getScope(), permissionRequired);
        // TODO check licence to create resource
        Paths.requireValidName(dao.getScope(), dao.getName());
        requireIdAvailability(Paths.resolve(dao.getScope(), dao.getName()));

        if (template == null) template = new Resource();
        template.setOwner(principal.getAccount());
        template.setScope(dao.getScope());
        template.adapt(dao, null);

        saveAndFlush(template.getInfo());
        Resource created = saveAndFlush(template);
        LOGGER.info(String.format("%s created. [principal=%s, created=%s]", typeLabel(), principal.getId(), created));
        return created;
    }

    public Resource update(Resource victim, ResourceDAO dao, Principal principal) {
        init(dao);
        victim.verifyAccess(dao, principal);
        ResourceDAO previous = victim.adapt(dao, null);
        flush();
        LOGGER.info(String.format("%s updated [principal=%s, victim=%s, previous=%s, updated=%s]", typeLabel(), principal.getId(), victim.getId(), previous, dao));
        return victim;
    }

    protected void init(ResourceDAO resourceDAO) {
        if (resourceDAO == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "no data");

        if (resourceDAO.getOwner() == null && resourceDAO.getOwnerId() != null) {
            Account account = SpringContext.getBean(AccountService.class)
                    .fetchById(resourceDAO.getOwnerId());
            resourceDAO.setOwner(account);
        }
        if (resourceDAO.getScope() == null && resourceDAO.getScopeId() != null) {
            Scope scope = SpringContext.getBean(ScopeService.class)
                    .fetchScope(resourceDAO.getOwnerId());
            resourceDAO.setScope(scope);
        }
    }

    protected Resource saveAndFlush(Resource created) {
        super.saveAndFlush(created);
        return resourceRepository.saveAndFlush(created);
    }

    protected Data saveAndFlush(Data created) {
        super.saveAndFlush(created);
        return dataRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        dataRepository.flush();
        resourceRepository.flush();
    }

    protected String typeLabel() {
        return "resource";
    }

}

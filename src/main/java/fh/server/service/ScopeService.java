package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Scope;
import fh.server.entity.access.AccessRule;
import fh.server.helpers.Paths;
import fh.server.repository.*;
import fh.server.rest.dao.AccessRuleDAO;
import fh.server.rest.dao.ScopeDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScopeService extends ComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScopeService.class);



    protected final ScopeRepository scopeRepository;
    protected final AccessRuleRepository accessRuleRepository;

    public ScopeService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository,
            @Qualifier("componentRepository") ComponentRepository componentRepository,
            @Qualifier("scopeRepository") ScopeRepository scopeRepository,
            @Qualifier("accessRuleRepository") AccessRuleRepository accessRuleRepository


    ) {
        super(entityRepository, resourceRepository, dataRepository, componentRepository);
        this.scopeRepository = scopeRepository;
        this.accessRuleRepository = accessRuleRepository;
    }




    public Scope fetchScope(String path) {
        return scopeRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "scope not found: "+path));
    }

    @Deprecated
    public Scope fetchScopeByName(String path) {
        return scopeRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "scope not found: " +path));
    }

    public AccessRule fetchAccessRule(String id) {
        return accessRuleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "access rule not found: "+id));
    }

    public Scope site(ScopeDAO dao, Account principal) {
        if (!principal.hasLicence("site-creator"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "premium plan required");
        Paths.requireValidName(dao.getScope(), dao.getName());
        requireIdAvailability(Paths.resolve(dao.getScope(), dao.getName()));
        dao.setPosition("default_0");

        Scope created = new Scope();
        created.setOwner(principal.getAccount());
        created.adapt(dao, null);

        saveAndFlush(created.getInfo());
        created = saveAndFlush(created);
        LOGGER.info(String.format("%s created. [principal=%s, created=%s]", typeLabel(), principal.getId(), created));
        return saveAndFlush(created);
    }

    public Scope create(Scope parent, ScopeDAO dao, Account principal) {
        dao.setScope(parent);
        init(dao);
        Scope created = (Scope) super.create(dao, principal, Permission.AuthorAccess, new Scope());
        return saveAndFlush(created);
    }

    public Scope update(Scope victim, ScopeDAO dao, Principal principal) {
        init(dao);
        victim = (Scope) super.update(victim, dao, principal);
        flush();
        return victim;
    }

    protected void init(ScopeDAO dao) {
        if (dao.getAccessRules() == null && dao.getAccessRuleIds() != null) {
            List<AccessRule> list = new ArrayList<>();
            for (String id : dao.getAccessRuleIds()) {
                if (id.startsWith("'")) try {
                    list.add(produce(dao.getAccessRuleDAOs().get(id)));
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "invalid reference: "+id);
                }
                else list.add(fetchAccessRule(id));
            }
            dao.setAccessRules(list);
        }
    }

    protected AccessRule produce(AccessRuleDAO dao) {
        AccessRule created = new AccessRule();
        created.setPermission(dao.getPermission());
        created.setAccessRuleType(dao.getAccessRuleType());
        created.setParam(dao.getParam());
        return accessRuleRepository.saveAndFlush(created);
    }

    protected Scope saveAndFlush(Scope created) {
        super.saveAndFlush(created);
        return scopeRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        scopeRepository.flush();
    }

    protected void deleteAndFlush(Scope arg0) { // todo not sure whether this is the correct order
        scopeRepository.delete(arg0);
        scopeRepository.flush();
        super.deleteAndFlush(arg0);
    }

    protected String typeLabel() {
        return "scope";
    }

}

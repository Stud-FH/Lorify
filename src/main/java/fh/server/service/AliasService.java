package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Scope;
import fh.server.helpers.Tokens;
import fh.server.repository.*;
import fh.server.rest.dao.AliasDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
public class AliasService extends ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasService.class);



    private final ScopeRepository scopeRepository;
    private final AliasRepository aliasRepository;

    public AliasService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository,
            @Qualifier("scopeRepository") ScopeRepository scopeRepository,
            @Qualifier("aliasRepository") AliasRepository aliasRepository
    ) {
        super(entityRepository, resourceRepository, dataRepository);
        this.scopeRepository = scopeRepository;
        this.aliasRepository = aliasRepository;
    }



    public Alias fetchAlias(String path) {
        return aliasRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found"));
    }

    public Alias mount(Scope scope, Account principal) {
        if (scope.hasAlias(principal.getId()))
            return scope.getAlias(principal);
        AliasDAO aliasDAO = new AliasDAO();
        aliasDAO.setScope(scope);
        aliasDAO.setName(principal.getName());
        aliasDAO.setAccount(principal);
        Alias created = (Alias) super.create(aliasDAO, principal, Permission.UserAccess, new Alias());
        created = saveAndFlush(created);
        scope.putAlias(created);
        scopeRepository.flush();
        return created;
    }

    public Alias create(Scope scope, AliasDAO dao, Principal principal) {
        dao.setScope(scope);
        dao.setToken(Tokens.shortAliasToken());
        Alias created = (Alias) super.create(dao, principal, Permission.AdminAccess, new Alias());
        created = saveAndFlush(created);
        scope.putAlias(created);
        scopeRepository.flush();
        return created;
    }

    public Alias claim(Scope site, AliasDAO dao, Account principal) {
        checkNotNull(dao);
        checkNotEmpty(dao.getToken(), "token");
        Alias alias;
        try {
            alias = site.claimAlias(dao.getToken(), principal);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "account is already mapped to an alias");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found");
        }
        aliasRepository.flush();
        scopeRepository.flush();
        LOGGER.info(String.format("alias claimed. [%s=%s, site=%s, principal=%s]", typeLabel(), alias, site, principal));
        return alias;
    }

    protected Alias saveAndFlush(Alias created) {
        super.saveAndFlush(created);
        return aliasRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        aliasRepository.flush();
    }

    protected String typeLabel() {
        return "alias";
    }

}

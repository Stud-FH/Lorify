package fh.server.service;

import fh.server.helpers.Operation;
import fh.server.constant.SiteVisibility;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Entity;
import fh.server.entity.Site;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dao.AliasDAO;
import fh.server.rest.dao.EntityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
public class AliasService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasService.class);



    private final SiteRepository siteRepository;
    private final AliasRepository aliasRepository;
    protected final AccountService accountService;

    public AliasService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository,
            @Qualifier("aliasRepository") AliasRepository aliasRepository,
            @Qualifier("accountService") AccountService accountService
    ) {
        super(entityRepository);
        this.siteRepository = siteRepository;
        this.aliasRepository = aliasRepository;
        this.accountService = accountService;
    }



    public Alias fetchById(String id) {
        return aliasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found"));
    }

    public Alias fetchByName(Site site, String name) {
        return aliasRepository.findByParentIdAndName(site.getId(), name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found"));
    }

    public Alias fetchByToken(Site site, String token) {
        Account account = accountService.fetchByToken(token);
        Alias alias = site.getAlias(account);
        if (alias != null) return alias;
        if (accountService.existsById(token))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found");
        checkNotEmpty(token, "Authorization header");
        alias = site.getAlias(token);
        if (alias != null) return alias;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found");
    }

    public Alias create(Site site, AliasDAO dao, Account principal) {
        Context context = new Context(principal, site);
        checkAliasCreator(context);
        checkNotNull(dao);
        checkNotEmpty(dao.getName(), "name");
        checkNameUnique(dao.getName(), context);

        Alias alias = site.createAlias(dao.getName());
        alias.setOwner(principal);
        alias.adapt(dao);
        alias = saveAndFlush(alias);
        siteRepository.flush();
        LOGGER.info(String.format("alias created. [%s=%s, site=%s, principal=%s]", typeLabel(), alias, site, principal));
        return alias;
    }

    public Alias claim(Site site, String token, Account principal) {
        checkNotEmpty(token, "token");
        Alias alias;
        try {
            alias = site.claimAlias(token, principal);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "account is already mapped to an alias");
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "token not found");
        }
        aliasRepository.flush();
        siteRepository.flush();
        LOGGER.info(String.format("alias claimed. [%s=%s, site=%s, principal=%s]", typeLabel(), alias, site, principal));
        return alias;
    }

    public Alias operate(Alias alias, AliasDAO dao, Entity principal) {
        super.operate(dao, new Context(principal, alias), this);
        return alias;
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {
        if ("name".equals(opKey)) {
            String name = ((AliasDAO) dao).getName();
            checkNotEmpty(name, "name");
            checkNameUnique(name, context);
            return context.operation(opKey, name, context.victimAsAlias().getName());
        }
        return super.setup(opKey, dao, context);
    }

    protected void execute(Operation operation, EntityDAO dao) {
        if (operation.getOperation().equals("name")) {
            operation.victimAsAlias().setName(operation.getValue());
        }
    }

    private void checkNameUnique(String name, Context context) {
        if (aliasRepository.existsByParentIdAndName(context.getVictim().getParentId(), name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name collision");
    }

    private void checkAliasCreator(Context context) {
        if (context.isOwnerAccess())
            return;

        if (context.victimAsSite().hasAlias(context.getPrincipal().getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you are already registered on this site");

        if (context.victimAsSite().getVisibility() == SiteVisibility.Private)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found");
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

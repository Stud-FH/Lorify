package fh.server.service;

import fh.server.constant.AliasManagementPolicy;
import fh.server.constant.SiteVisibility;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Site;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
public class AliasService extends ArtifactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasService.class);



    private final SiteRepository siteRepository;
    private final AliasRepository aliasRepository;
    protected final AccountService accountService;

    public AliasService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository,
            @Qualifier("siteRepository") SiteRepository siteRepository,
            @Qualifier("aliasRepository") AliasRepository aliasRepository,
            @Qualifier("accountService") AccountService accountService
    ) {
        super(entityRepository, artifactRepository);
        this.siteRepository = siteRepository;
        this.aliasRepository = aliasRepository;
        this.accountService = accountService;
    }



    public Alias fetchById(String id) {
        return aliasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found"));
    }

    public Alias fetchByName(Site site, String name) {
        return aliasRepository.findBySiteIdAndName(site.getId(), name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found"));
    }

    public Alias fetchByToken(Site site, String token) {
        checkNotEmpty(token, "Authorization header");
        Alias alias = site.getAlias(token);
        if (alias != null) return alias;
        Account account = accountService.fetchByToken(token);
        alias = site.getAlias(account);
        if (alias != null) return alias;
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "alias not found");
    }

    /**
     * @return  site, principal, alias, artifact, entity
     */
    public Context buildContext(Alias alias, Account principal) {
        Site site = siteRepository.getById(alias.getSiteId());
        return Context.build()
                .principal(principal)
                .site(site)
                .alias(alias)
                .artifact(alias)
                .entity(alias)
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

    public Alias create(Site site, AliasBlueprint blueprint, Account principal) {
        Context context = buildContext(site, principal);
        checkNotNull(blueprint);
        checkNotEmpty(blueprint.getName(), "name");
        checkNameUnique(blueprint.getName(), context);
        checkAliasCreator(context);

        Alias alias = site.createAlias(blueprint.getName());
        alias.addOwner(principal);
        alias.putAttributes(blueprint.getAttributes());
        alias.addTags(blueprint.getTags());
        alias.setComments(blueprint.getComments());
        alias.setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());
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

    public Alias update(AliasBlueprint blueprint, Account principal) {
        checkNotNull(blueprint);
        Alias alias = fetchById(blueprint.getId());
        super.update(blueprint, buildContext(alias, principal), this);
        return alias;
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.verifyUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((AliasService) service).verifyNameUpdate(((AliasBlueprint) blueprint), context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.performUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((AliasService) service).performNameUpdate(((AliasBlueprint) blueprint), context);
        }
    }

    public void verifyNameUpdate(AliasBlueprint blueprint, Context context) {
        checkNotNull(context.getAlias());
        checkNotNull(context.getSite());
        checkNameManagementPolicy(context);
        checkNotEmpty(blueprint.getName(), "name");
        checkNameUnique(blueprint.getName(), context);
    }

    public void performNameUpdate(AliasBlueprint blueprint, Context context) {
        String previous = context.getAlias().getName();
        context.getAlias().setName(blueprint.getName());
        aliasRepository.flush();
        LOGGER.info(String.format("name updated: %s -> %s in %s", previous, blueprint.getName(), context));
    }

    /**
     * @param context site
     */
    private void checkNameUnique(String name, Context context) {
        if (aliasRepository.existsBySiteIdAndName(context.getSite().getId(), name))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name collision");
    }

    /**
     * @param context site, principal
     */
    private void checkAliasCreator(Context context) {
        if (context.getSite().hasOwner(context.getPrincipal()))
            return;

        if (context.getSite().hasAlias(context.getPrincipal()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you are already registered on this site");

        if (context.getSite().getVisibility() == SiteVisibility.Private)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "site not found");
    }

    private void checkNameManagementPolicy(Context context) {
        checkAliasManagementPolicy(context.getSite().getNameManagementPolicy(), context);
    }

    private void checkTagManagementPolicy(Context context) {
        checkAliasManagementPolicy(context.getSite().getTagManagementPolicy(), context);
    }

    private void checkAttributeManagementPolicy(Context context) {
        checkAliasManagementPolicy(context.getSite().getAttributeManagementPolicy(), context);
    }

    /**
     * @param context site, principal, alias
     */
    private void checkAliasManagementPolicy(AliasManagementPolicy policy, Context context) {
        if (!(policy.approvesUser() && context.getAlias().equals(context.getSite().getAlias(context.getPrincipal())))
                && !(policy.approvesAdmin() && context.getSite().hasOwner(context.getPrincipal())))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "access denied");
    }

    protected Alias saveAndFlush(Alias created) {
        super.saveAndFlush(created);
        return aliasRepository.saveAndFlush(created);
    }

    protected String typeLabel() {
        return "alias";
    }

}

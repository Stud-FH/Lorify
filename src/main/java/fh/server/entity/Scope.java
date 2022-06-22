package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.Permission;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.entity.access.AccessRule;
import fh.server.rest.dao.ResourceDAO;
import fh.server.rest.dao.ScopeDAO;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Scope extends Component {

    @OneToMany(orphanRemoval = true)
    private final Map<String, Alias> aliases = new HashMap<>();

    @ManyToMany
    private final List<AccessRule> accessRules = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "scope_components",
            joinColumns = {@JoinColumn(name = "scope_id")},
            inverseJoinColumns = {@JoinColumn(name = "component_id")}
    )
    private final Set<Component> components = new LinkedHashSet<>();


    public Map<String, Alias> getAliases() {
        return aliases;
    }

    public Alias getAlias(String accessor) {
        Alias alias = aliases.get(accessor);
        if (alias != null) return alias;
        return super.getAlias(accessor);
    }

    public boolean hasAlias(String accessor) {
        return aliases.containsKey(accessor) || (hasParent() && getScope().hasAlias(accessor));
    }

    public void putAlias(Alias alias) {
        aliases.put(alias.hasAccount()? alias.getAccount().getId() : alias.getToken(), alias);
        setLastModified(System.currentTimeMillis());
    }

    public Alias claimAlias(String token, Account account) {
        if (hasAlias(account.getId())) throw new IllegalStateException();
        Alias alias = aliases.remove(token);
        if (alias == null) throw new NoSuchElementException();
        alias.setAccount(account);
        aliases.put(account.getId(), alias);
        setLastModified(System.currentTimeMillis());
        return alias;
    }

    public void removeAliases(Set<String> aliases) {
        if (aliases == null) return;
        aliases.forEach(this.aliases::remove);
        setLastModified(System.currentTimeMillis());
    }

    public List<AccessRule> getAccessRules() {
        return accessRules;
    }

    public void setAccessRules(List<AccessRule> accessRules) {
        this.accessRules.clear();
        this.accessRules.addAll(accessRules);
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void removeComponents(Set<String> components) {
        if (components == null) return;
        this.components.removeIf(c -> components.contains(c.getName()));
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public PermissionSetting verifyAccess(ResourceDAO resourceDAO, Principal principal) {
        PermissionSetting ps = super.verifyAccess(resourceDAO, principal);
        ScopeDAO dao = (ScopeDAO) resourceDAO;

        if (dao.getAntiAliases() != null) {
            ps.getResource().require(Permission.ExclusiveAccess);
        }
        if (dao.getAntiComponents() != null) {
            ps.getStronger().require(Permission.ExclusiveAccess);
        }
        if (dao.getAccessRules() != null) {
            ps.getResource().require(Permission.ExclusiveAccess);
        }
        return ps;
    }

    @Override
    public ScopeDAO adapt(ResourceDAO resourceDAO, ResourceDAO logObject) {
        if (logObject == null) logObject = new ScopeDAO();
        super.adapt(resourceDAO, logObject);
        ScopeDAO dao = (ScopeDAO) resourceDAO;
        ScopeDAO lo = (ScopeDAO) logObject;

        if (dao.getAntiAliases() != null) {
            lo.setAliases(aliases);
            removeAliases(dao.getAntiAliases());
        }
        if (dao.getAntiComponents() != null) {
            lo.setComponents(components);
            removeComponents(dao.getAntiComponents());
        }
        if (dao.getAccessRules() != null) {
            lo.setAccessRules(accessRules);
            setAccessRules(dao.getAccessRules());
        }
        return lo;
    }

    public Permission getPermission(Principal principal) {
        for (AccessRule rule : accessRules) {
            Permission result = rule.getPermission(this, principal);
            if (result != null) return result;
        }
        if (hasParent()) return getScope().getPermission(principal);
        return Permission.UserView;
    }

    public void setName(String name) {
        super.setName(name);
        aliases.values().forEach(Resource::updatePath);
        components.forEach(Resource::updatePath);
    }

    @Override
    public EntityType getType() {
        return EntityType.Scope;
    }
}

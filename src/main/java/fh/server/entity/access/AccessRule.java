package fh.server.entity.access;

import fh.server.constant.AccessRuleType;
import fh.server.constant.EntityType;
import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Entity;
import fh.server.entity.Scope;

import javax.persistence.Column;
import javax.persistence.Enumerated;

@javax.persistence.Entity
public class AccessRule extends Entity {

    @Enumerated
    @Column(nullable = false)
    private Permission permission;

    @Enumerated
    @Column(nullable = false)
    private AccessRuleType accessRuleType;

    @Column
    private String param;




    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
        setLastModified(System.currentTimeMillis());
    }

    public AccessRuleType getAccessRuleType() {
        return accessRuleType;
    }

    public void setAccessRuleType(AccessRuleType accessRuleType) {
        this.accessRuleType = accessRuleType;
        setLastModified(System.currentTimeMillis());
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
        setLastModified(System.currentTimeMillis());
    }

    public Permission getPermission(Scope scope, Principal principal) {
        return test(scope, principal)? permission : null;
    }

    protected boolean test(Scope scope, Principal principal) {
        switch (accessRuleType) {
            case FlagAbsent: return !principal.getAlias(scope).getFlags().contains(param);
            case FlagPresent: return principal.getAlias(scope).getFlags().contains(param);
            case Default: return true;
            default: return false;
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.AccessRule;
    }
}

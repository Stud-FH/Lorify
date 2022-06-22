package fh.server.context;

import fh.server.constant.Permission;

public class PermissionSetting {

    private final Principal principal;
    private final Permission resource, scope, stronger;

    public PermissionSetting(Principal principal, Permission resource, Permission scope) {
        this.principal = principal;
        this.resource = resource;
        this.scope = scope;
        stronger = resource.fails(scope)? scope : resource;
    }


    public Principal getPrincipal() {
        return principal;
    }

    public Permission getResource() {
        return resource;
    }

    public Permission getScope() {
        return scope;
    }

    public Permission getStronger() {
        return stronger;
    }
}

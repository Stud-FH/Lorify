package fh.server.rest.dao;

import fh.server.constant.AccessRuleType;
import fh.server.constant.Permission;

public class AccessRuleDAO extends EntityDAO {

    private Permission permission;
    private AccessRuleType accessRuleType;
    private String param;


    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public AccessRuleType getAccessRuleType() {
        return accessRuleType;
    }

    public void setAccessRuleType(AccessRuleType accessRuleType) {
        this.accessRuleType = accessRuleType;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}

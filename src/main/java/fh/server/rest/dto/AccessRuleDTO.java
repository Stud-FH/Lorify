package fh.server.rest.dto;

import fh.server.constant.AccessRuleType;
import fh.server.constant.Permission;
import fh.server.entity.access.AccessRule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRuleDTO extends EntityDTO {

    private Permission permission;
    private AccessRuleType accessRuleType;
    private String param;

    public AccessRuleDTO(AccessRule source) {
        super(source);
        permission = source.getPermission();
        accessRuleType = source.getAccessRuleType();
        param = source.getParam();
    }

    public Permission getPermission() {
        return permission;
    }

    public AccessRuleType getAccessRuleType() {
        return accessRuleType;
    }

    public String getParam() {
        return param;
    }
}
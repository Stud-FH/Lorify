package fh.server.rest.dto;


import fh.server.constant.Permission;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.entity.Resource;

import java.util.Map;
import java.util.Set;

public class ResourceDTO extends EntityDTO {

    private String name;
    private String path;
    private String description;
    private DataDTO info;
    private String ownerId;
    private String scopeId;

    private Set<String> tags;
    private Set<String> flags;
    private Map<String, String> attributes;
    private Map<String, String> properties;

    protected final PermissionSetting ps;


    public ResourceDTO(Resource source, Principal principal) {
        super(source);
        ps = source.getPermissionSetting(principal);
        if (ps.getResource().meets(Permission.UserView)) {
            name = source.getName();
            path = source.getPath();
            description = source.getDescription();
            ownerId = source.getOwner().getId();
            scopeId = source.hasParent()? source.getScope().getId() : null;
            tags = source.getTags();
            attributes = source.getAttributes();
        }
        if (ps.getResource().meets(Permission.AuthorView)) {
            info = new DataDTO(source.getInfo());
        }
        if (ps.getResource().fails(Permission.AdminView)) {
            flags = source.getFlags();
            properties = source.getProperties();
        }

    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public DataDTO getInfo() {
        return info;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Set<String> getFlags() {
        return flags;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}

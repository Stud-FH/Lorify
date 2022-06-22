package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.Permission;
import fh.server.constant.SpringContext;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.helpers.Paths;
import fh.server.rest.dao.ResourceDAO;
import fh.server.service.EntityService;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Resource extends Entity {

    protected static final int NAME_LENGTH = 50;
    protected static final long NAME_SIZE = NAME_LENGTH +1;
    protected static final int PATH_LENGTH = 255;
    protected static final long PATH_SIZE = PATH_LENGTH +1;
    protected static final int DESC_LENGTH = 255;
    protected static final long DESC_SIZE = DESC_LENGTH +2;
    protected static final int TAG_LENGTH = 50;
    protected static final long TAG_SIZE = TAG_LENGTH +1;
    protected static final int FLAG_LENGTH = 50;
    protected static final long FLAG_SIZE = FLAG_LENGTH +1;

    protected static final long ATTRIBUTE_SIZE = 524;
    protected static final long PROPERTY_SIZE = 524;

    protected static final long RAW_RESOURCE_SIZE = RAW_ENTITY_SIZE + NAME_SIZE + PATH_SIZE + DESC_SIZE + 4*REF_SIZE;



    @Column(nullable = false, length = NAME_LENGTH)
    private String name;

    @Column(unique = true, nullable = false, length = PATH_LENGTH)
    private String path;

    @Column(length = DESC_LENGTH)
    private String description;

    @OneToOne(orphanRemoval = true)
    @JoinColumn
    private final Data info = new Data();

    @ManyToOne
    private Account owner;

    @ManyToOne
    @JoinColumn
    private Scope scope;

    @ElementCollection
    @Column(length = TAG_LENGTH)
    private final Set<String> tags = new LinkedHashSet<>();

    @ElementCollection
    @Column(length = FLAG_LENGTH)
    private final Set<String> flags = new LinkedHashSet<>();

    @ElementCollection
    private final Map<String, String> attributes = new HashMap<>();

    @ElementCollection
    private final Map<String, String> properties = new HashMap<>();




    public String getName() {
        return name;
    }

    /**
     * also updates the resulting path!
     */
    public void setName(String name) {
        this.name = name;
        updatePath();
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
        setLastModified(System.currentTimeMillis());
    }

    public Data getInfo() {
        return info;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
        setLastModified(System.currentTimeMillis());
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
        setLastModified(System.currentTimeMillis());
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTags(Collection<String> tags) {
        if (tags == null) return;
        this.tags.addAll(tags);
        setLastModified(System.currentTimeMillis());
    }

    public void removeTags(Collection<String> tags) {
        if (tags == null) return;
        this.tags.removeAll(tags);
        setLastModified(System.currentTimeMillis());
    }

    public Set<String> getFlags() {
        return flags;
    }

    public void addFlags(Collection<String> flags) {
        if (flags == null) return;
        this.flags.addAll(flags);
        setLastModified(System.currentTimeMillis());
    }

    public void removeFlags(Collection<String> flags) {
        if (flags == null) return;
        this.flags.removeAll(flags);
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void putAttributes(Map<String, String> attributes) {
        if (attributes == null) return;
        this.attributes.putAll(attributes);
        setLastModified(System.currentTimeMillis());
    }

    public void removeAttributes(Collection<String> attributes) {
        if (attributes == null) return;
        attributes.forEach(this.attributes::remove);
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void putProperties(Map<String, String> properties) {
        if (properties == null) return;
        this.properties.putAll(properties);
        setLastModified(System.currentTimeMillis());
    }

    public void removeProperties(Collection<String> properties) {
        if (properties == null) return;
        properties.forEach(this.properties::remove);
        setLastModified(System.currentTimeMillis());
    }

    protected void updatePath() {
        path = Paths.resolve(scope, name);
    }

    public long size() {
        return  RAW_ENTITY_SIZE
                + RAW_RESOURCE_SIZE
                + info.size()
                + TAG_SIZE * tags.size()
                + FLAG_SIZE * flags.size()
                + ATTRIBUTE_SIZE * attributes.size()
                + PROPERTY_SIZE * properties.size();
    }

    public boolean hasParent() {
        return scope != null;
    }

    public boolean inherits(Resource parent) {
        return equals(parent) || (hasParent() && scope.inherits(parent));
    }

    public Alias getAlias(String accessor) {
        if (scope != null) return scope.getAlias(accessor);
        throw new NoSuchElementException();
    }

    public boolean hasAlias(Account account) {
        return getAlias(account) != null; // todo bad performance
    }

    public Alias getAlias(Account account) {
        return getAlias(account.getId());
    }

    public Permission getPermission(Principal principal) {
        return owner.equals(principal)? Permission.ExclusiveAccess : Permission.UserView;
    }

    public PermissionSetting getPermissionSetting(Principal principal) {
        Permission resourcePermission = getPermission(principal);
        Permission scopePermission = scope != null? scope.getPermission(principal) : resourcePermission;
        return new PermissionSetting(principal, resourcePermission, scopePermission);
    }

    public PermissionSetting verifyAccess(ResourceDAO resourceDAO, Principal principal) {
        PermissionSetting ps = getPermissionSetting(principal);

        if (resourceDAO.getName() != null) {
            ps.getStronger().require(Permission.ExclusiveAccess);
            Paths.requireValidName(scope, resourceDAO.getName());
            SpringContext.getBean(EntityService.class)
                    .requireIdAvailability(Paths.resolve(scope, resourceDAO.getName()));
        }
        if (resourceDAO.getDescription() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getInfo() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getOwner() != null) {
            ps.getResource().require(Permission.ExclusiveAccess);
        }
        if (resourceDAO.getTags() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getFlags() != null) {
            ps.getScope().require(Permission.AdminAccess);
        }
        if (resourceDAO.getAttributes() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getProperties() != null) {
            ps.getScope().require(Permission.AdminAccess);
        }
        if (resourceDAO.getAntiTags() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getAntiFlags() != null) {
            ps.getScope().require(Permission.AdminAccess);
        }
        if (resourceDAO.getAntiAttributes() != null) {
            ps.getStronger().require(Permission.AuthorAccess);
        }
        if (resourceDAO.getAntiProperties() != null) {
            ps.getScope().require(Permission.AdminAccess);
        }

        return ps;
    }

    public ResourceDAO adapt(ResourceDAO resourceDAO, ResourceDAO logObject) {
        if (logObject == null) logObject = new ResourceDAO();

        if (resourceDAO.getName() != null) {
            logObject.setName(name);
            setName(resourceDAO.getName());
        }
        if (resourceDAO.getDescription() != null) {
            logObject.setDescription(description);
            setDescription(resourceDAO.getDescription());
        }
        if (resourceDAO.getInfo() != null) {
            logObject.setInfo(info.readString());
            info.writeString(resourceDAO.getInfo());
        }
        if (resourceDAO.getOwner() != null) {
            logObject.setOwnerId(owner.getId());
            setOwner(resourceDAO.getOwner());   // TODO disable possibility to fill other users' data capacity
        }
        if (resourceDAO.getTags() != null) {
            logObject.setTags(tags);
            addTags(resourceDAO.getTags());
        }
        if (resourceDAO.getFlags() != null) {
            logObject.setFlags(flags);
            addFlags(resourceDAO.getFlags());
        }
        if (resourceDAO.getAttributes() != null) {
            logObject.setAttributes(attributes);
            putAttributes(resourceDAO.getAttributes());
        }
        if (resourceDAO.getProperties() != null) {
            logObject.setProperties(properties);
            putProperties(resourceDAO.getProperties());
        }
        if (resourceDAO.getAntiTags() != null) {
            if (logObject.getTags() == null) logObject.setTags(tags);
            removeTags(resourceDAO.getAntiTags());
        }
        if (resourceDAO.getAntiFlags() != null) {
            if (logObject.getFlags() == null) logObject.setFlags(flags);
            removeFlags(resourceDAO.getAntiFlags());
        }
        if (resourceDAO.getAntiAttributes() != null) {
            if (logObject.getAttributes() == null) logObject.setAttributes(attributes);
            removeAttributes(resourceDAO.getAntiAttributes());
        }
        if (resourceDAO.getAntiProperties() != null) {
            if (logObject.getProperties() == null) logObject.setProperties(properties);
            removeProperties(resourceDAO.getAntiProperties());
        }

        return logObject;
    }

    @Override
    public EntityType getType() {
        return EntityType.Resource;
    }
}

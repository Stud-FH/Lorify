package fh.server.rest.dao;


import fh.server.entity.Account;
import fh.server.entity.Scope;

import java.util.Map;
import java.util.Set;

public class ResourceDAO extends EntityDAO {


    private String name;
    private String description;
    private String info;

    private String ownerId;
    private String scopeId;

    private Account owner;
    private Scope scope;

    private Set<String> tags;
    private Set<String> flags;
    private Map<String, String> attributes;
    private Map<String, String> properties;

    private Set<String> antiTags;
    private Set<String> antiFlags;
    private Set<String> antiAttributes;
    private Set<String> antiProperties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getFlags() {
        return flags;
    }

    public void setFlags(Set<String> flags) {
        this.flags = flags;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Set<String> getAntiTags() {
        return antiTags;
    }

    public void setAntiTags(Set<String> antiTags) {
        this.antiTags = antiTags;
    }

    public Set<String> getAntiFlags() {
        return antiFlags;
    }

    public void setAntiFlags(Set<String> antiFlags) {
        this.antiFlags = antiFlags;
    }

    public Set<String> getAntiAttributes() {
        return antiAttributes;
    }

    public void setAntiAttributes(Set<String> antiAttributes) {
        this.antiAttributes = antiAttributes;
    }

    public Set<String> getAntiProperties() {
        return antiProperties;
    }

    public void setAntiProperties(Set<String> antiProperties) {
        this.antiProperties = antiProperties;
    }
}

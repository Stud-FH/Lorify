package fh.server.rest.dao;

import fh.server.constant.TrustLevel;

import java.util.Map;
import java.util.Set;

public class EntityDAO {

    private String id;

    private String parentId;

    private String ownerId;

    private Map<String, String> attributes;

    private Map<String, TrustLevel> accessRequirements;

    private Map<String, String> guards;

    private Set<String> changelist;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, TrustLevel> getAccessRequirements() {
        return accessRequirements;
    }

    public void setAccessRequirements(Map<String, TrustLevel> accessRequirements) {
        this.accessRequirements = accessRequirements;
    }

    public Map<String, String> getGuards() {
        return guards;
    }

    public void setGuards(Map<String, String> guards) {
        this.guards = guards;
    }

    public Set<String> getOreationKeys() {
        return changelist;
    }

    public void setChangelist(Set<String> changelist) {
        this.changelist = changelist;
    }
}

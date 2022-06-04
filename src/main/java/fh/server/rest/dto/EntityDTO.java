package fh.server.rest.dto;

import fh.server.constant.TrustLevel;
import fh.server.entity.Entity;

import java.util.Map;

public class EntityDTO {

    private String id;
    private String parentId;
    private String ownerId;
    private Map<String, String> attributes;
    protected Map<String, TrustLevel> accessRequirements;
    private Map<String, String> guards;
    private Long lastModified;





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

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public void adapt(Entity source) {
        setId(source.getId());
        setParentId(source.getParentId());
        setOwnerId(source.getOwnerId());
        setAttributes(source.getAttributes());
        setAccessRequirements(source.getAccessRequirements());
        setGuards(source.getGuards());
        setLastModified(source.getLastModified());
    }

    public EntityDTO prune(TrustLevel principalClearance) {
        if (!principalClearance.meets(accessRequirements.get("get-id"))) id = null;
        if (!principalClearance.meets(accessRequirements.get("get-parentId"))) parentId = null;
        if (!principalClearance.meets(accessRequirements.get("get-ownerId"))) ownerId = null;
        if (!principalClearance.meets(accessRequirements.get("get-attributes"))) attributes = null;
        else for(String k : attributes.keySet()) if (!principalClearance.meets(accessRequirements.get("get-a:"+k))) attributes.remove(k);
        if (!principalClearance.meets(accessRequirements.get("get-guards"))) guards = null;
        else for(String k : guards.keySet()) if (!principalClearance.meets(accessRequirements.get("get-g:"+k))) guards.remove(k);
        if (!principalClearance.meets(accessRequirements.get("get-lastModified"))) lastModified = null;
        if (!principalClearance.meets(accessRequirements.get("get-accessRequirements"))) accessRequirements = null;
        return this;
    }

}

package fh.server.rest.dto;

import java.util.Map;
import java.util.Set;

public class EntityBlueprint {

    private String id;

    private Set<String> ownerIds;

    private Map<String, String> attributes;

    private Set<String> changelist;

    private Set<String> attributeKeys;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(Set<String> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Set<String> getChangelist() {
        return changelist;
    }

    public void setChangelist(Set<String> changelist) {
        this.changelist = changelist;
    }

    public Set<String> getAttributeKeys() {
        return attributeKeys;
    }

    public void setAttributeKeys(Set<String> attributeKeys) {
        this.attributeKeys = attributeKeys;
    }

}

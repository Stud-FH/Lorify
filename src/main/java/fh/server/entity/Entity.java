package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.TrustLevel;
import fh.server.helpers.Context;
import fh.server.helpers.interpreter.GStream;
import fh.server.helpers.interpreter.Interpreter;
import fh.server.rest.dao.EntityDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Entity implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column
    private String parentId;

    @Column(nullable = false)
    private String ownerId;

    @ElementCollection
    private final Map<String, String> attributes = new HashMap<>();

    @ElementCollection
    private final Map<String, TrustLevel> accessRequirements = new HashMap<>();

    @ElementCollection // TODO length restriction
    private final Map<String, String> guards = new HashMap<>();

    @Column(nullable = false)
    private Long lastModified = System.currentTimeMillis();

    @Transient
    private transient Entity parent;

    @Transient
    private transient Map<String, GStream> decodedGuards;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setLastModified(System.currentTimeMillis());
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
        setLastModified(System.currentTimeMillis());
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        setLastModified(System.currentTimeMillis());
    }

    public void setOwner(Account owner) {
        this.ownerId = owner.getId();
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public boolean hasTag(String tag) {
        return Boolean.parseBoolean(attributes.get(tag));
    }

    public String putAttribute(String key, String value) {
        if (key == null || key.isEmpty()) return null;
        setLastModified(System.currentTimeMillis());
        if (value == null) return attributes.remove(key);
        return attributes.put(key, value);
    }

    public String removeAttribute(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return attributes.remove(key);
    }

    public void putAttributes(Map<String, String> attributes) {
        if (attributes == null) return;
        this.attributes.putAll(attributes);
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, TrustLevel> getAccessRequirements() {
        return accessRequirements;
    }

    public TrustLevel getAccessRequirement(String key) {
        TrustLevel result = accessRequirements.get(key);
        if (result != null) return result;
        Entity parent = getParent();
        return parent == null? getDefaultAccessRequirement() : parent.getAccessRequirement(key);
    }

    public TrustLevel getDefaultAccessRequirement() {
        TrustLevel result = accessRequirements.get("default");
        if (result != null) return result;
        Entity parent = getParent();
        return parent == null? TrustLevel.Viewer : parent.getDefaultAccessRequirement();
    }

    public TrustLevel putAccessRequirements(String key, TrustLevel value) {
        if (key == null || key.isEmpty()) return null;
        setLastModified(System.currentTimeMillis());
        if (value == null) return accessRequirements.remove(key);
        return accessRequirements.put(key, value);
    }

    public TrustLevel removeAccessRequirement(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return accessRequirements.remove(key);
    }

    public void putAccessRequirements(Map<String, TrustLevel> accessRequirements) {
        if (accessRequirements == null) return;
        this.accessRequirements.putAll(accessRequirements);
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, String> getGuards() {
        return guards;
    }

    public String getGuard(String key) {
        return guards.get(key);
    }

    public String getDefaultGuard() {
        return guards.get("default");
    }

    public GStream getDecodedGuard(String key) {
        if (decodedGuards == null) decodedGuards = new HashMap<>();
        GStream result = decodedGuards.computeIfAbsent(key, k -> Interpreter.resilientGStream(guards.get(key)));
        if (result != null) return result;
        Entity parent = getParent();
        return parent == null? getDecodedDefaultGuard() : parent.getDecodedGuard(key);
    }

    public GStream getDecodedDefaultGuard() {
        if (decodedGuards == null) decodedGuards = new HashMap<>();
        GStream result = decodedGuards.get("default");
        if (result != null) return result;
        Entity parent = getParent();
        return parent == null? GStream.defaultRuling : parent.getDecodedDefaultGuard();
    }

    public String putGuard(String key, String value) {
        if (key == null || key.isEmpty()) return null;
        if (value == null) return removeGuard(key);
        setLastModified(System.currentTimeMillis());
        return guards.put(key, value);
    }

    public void putGuards(Map<String, String> guards) {
        if (guards == null) return;
        this.guards.putAll(guards);
        setLastModified(System.currentTimeMillis());
    }

    public String removeGuard(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return guards.remove(key);
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public void adapt(EntityDAO dao) {
        putAttributes(dao.getAttributes());
        putGuards(dao.getGuards());
        putAccessRequirements(dao.getAccessRequirements());
    }

    protected Entity fetchParent() {
        return null;
    }

    public Entity getParent() {
        if (parent == null && parentId != null) parent = fetchParent();
        return parent;
    }

    public TrustLevel getPruningClearance(Entity principal) {
        return getDecodedDefaultGuard().authenticate(new Context(principal, this).operation("get", null, null));
    }

    public abstract EntityType getType();

    @Override
    public String toString() {
        return String.format("entity#%s", id);
    }
}

package fh.server.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Entity implements Serializable {

    @Id
    private String id = UUID.randomUUID().toString();

    @ElementCollection
    private final Set<String> ownerIds = new HashSet<>();

    @ElementCollection
    private final Map<String, String> attributes = new HashMap<>();

    @Column
    private Long lastModified;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setLastModified(System.currentTimeMillis());
    }

    public Set<String> getOwnerIds() {
        return ownerIds;
    }

    public boolean hasOwner(Account account) {
        return ownerIds.contains(account.getId());
    }

    public void addOwner(Account account) {
        if (account == null) return;
        ownerIds.add(account.getId());
        setLastModified(System.currentTimeMillis());
    }

    public void addOwner(Collection<String> accountIds) {
        if (accountIds == null) return;
        ownerIds.addAll(accountIds);
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

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return String.format("entity#%s", id);
    }
}

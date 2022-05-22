package fh.server.entity;

import fh.server.helpers.interpreter.B;
import fh.server.helpers.interpreter.DescriptionInterpreter;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Artifact extends Entity {

    @Column(length = 2000)
    private String comments; // organisational tool for mods; invisible to users.

    @Column(length = 2000)
    private String visibilityGuardDescription;

    @Transient
    private transient B visibilityGuard;

    @ElementCollection
    private final Set<String> tags = new HashSet<>();




    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
        setLastModified(System.currentTimeMillis());
    }

    public String getVisibilityGuardDescription() {
        return visibilityGuardDescription;
    }

    public void setVisibilityGuardDescription(String s) {
        this.visibilityGuardDescription = s;
        setLastModified(System.currentTimeMillis());
    }

    public B getCreatorGuard() {
        if (visibilityGuard == null) {
            visibilityGuard = DescriptionInterpreter.resilientB(visibilityGuardDescription);
        }
        return visibilityGuard;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
        setLastModified(System.currentTimeMillis());
    }

    public void removeTag(String tag) {
        tags.remove(tag);
        setLastModified(System.currentTimeMillis());
    }

    public void addTags(Collection<String> tags) {
        if (tags == null || tags.isEmpty()) return;
        this.tags.addAll(tags);
        setLastModified(System.currentTimeMillis());
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}

package fh.server.rest.dto;

import java.util.Set;

public class ArtifactDTO extends EntityDTO {

    private Set<String> ownerIds;

    private String comments;

    private String visibilityGuard;

    private Set<String> tags;


    public Set<String> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(Set<String> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVisibilityGuard() {
        return visibilityGuard;
    }

    public void setVisibilityGuard(String visibilityGuard) {
        this.visibilityGuard = visibilityGuard;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}

package fh.server.rest.dto;

import java.util.Set;

public class ArtifactBlueprint extends EntityBlueprint {

    private String comments;

    private String visibilityGuardDescription;

    private Set<String> tags;

    private Set<String> antiTags;


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVisibilityGuardDescription() {
        return visibilityGuardDescription;
    }

    public void setVisibilityGuardDescription(String visibilityGuardDescription) {
        this.visibilityGuardDescription = visibilityGuardDescription;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<String> getAntiTags() {
        return antiTags;
    }

    public void setAntiTags(Set<String> antiTags) {
        this.antiTags = antiTags;
    }


}

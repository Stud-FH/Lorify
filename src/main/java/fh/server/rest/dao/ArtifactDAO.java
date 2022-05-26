package fh.server.rest.dao;

import java.util.Set;

public class ArtifactDAO extends EntityDAO {

    private String comments;

    private String visibilityGuard;

    private Set<String> tags;

    private Set<String> antiTags;


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

    public Set<String> getAntiTags() {
        return antiTags;
    }

    public void setAntiTags(Set<String> antiTags) {
        this.antiTags = antiTags;
    }


}

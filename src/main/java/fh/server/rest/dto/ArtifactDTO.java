package fh.server.rest.dto;

import fh.server.entity.Account;
import fh.server.rest.mapper.DTOMapper;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ArtifactDTO extends EntityDTO {

    private Set<String> ownerIds;

    private String comments;

    private String visibilityGuardDescription;

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
}

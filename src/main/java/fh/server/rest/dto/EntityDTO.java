package fh.server.rest.dto;

import fh.server.entity.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityDTO {

    private String id;
    private Long lastModified;

    public EntityDTO(Entity source) {
        id = source.getId();
        lastModified = source.getLastModified();
    }

    public String getId() {
        return id;
    }

    public Long getLastModified() {
        return lastModified;
    }
}

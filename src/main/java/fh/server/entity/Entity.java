package fh.server.entity;

import fh.server.constant.EntityType;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@javax.persistence.Entity
@Table(name = "entity", schema = "dbo")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Entity implements Serializable {


    public static final int ID_LENGTH = 255;

    protected static final long REF_SIZE = 8;
    protected static final long ID_SIZE = ID_LENGTH +2;
    protected static final long LAST_MODIFIED_SIZE = 8;
    protected static final long RAW_ENTITY_SIZE = ID_SIZE + LAST_MODIFIED_SIZE;

    @Id
    @Column(nullable = false, length = ID_LENGTH)
    private String id = getType() + "#" + UUID.randomUUID();

    @Column(nullable = false)
    private Long lastModified = System.currentTimeMillis();




    public String getId() {
        return id;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    protected Entity fetchParent() {
        return null;
    }

    public abstract EntityType getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}

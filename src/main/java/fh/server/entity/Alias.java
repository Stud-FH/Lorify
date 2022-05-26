package fh.server.entity;

import fh.server.constant.EntityType;

import javax.persistence.*;

@javax.persistence.Entity
public class Alias extends Entity {

    @Column(nullable = false)
    private String name;

    @Column
    private String accessor;

    @Column(nullable = false)
    private Boolean claimed = false;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastModified(System.currentTimeMillis());
    }

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
    }

    public Boolean getClaimed() {
        return claimed;
    }

    /**
     * only for pruning; never use this for DB-entities!
     */
    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    public void claim(Account account) {
        this.accessor = account.getId();
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public EntityType getType() {
        return EntityType.Alias;
    }
}

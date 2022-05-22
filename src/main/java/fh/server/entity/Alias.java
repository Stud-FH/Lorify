package fh.server.entity;

import javax.persistence.*;

@javax.persistence.Entity
public class Alias extends Artifact {

    @Column(nullable = false)
    private String siteId;

    @Column
    private String accessor;

    @Column(nullable = false)
    private String name;




    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
        setLastModified(System.currentTimeMillis());
    }

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String siteToken) {
        this.accessor = siteToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastModified(System.currentTimeMillis());
    }
}

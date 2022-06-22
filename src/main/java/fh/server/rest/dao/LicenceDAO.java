package fh.server.rest.dao;

import fh.server.entity.Account;

import java.util.Set;

public class LicenceDAO extends EntityDAO {

    private Set<String> privileges;
    private Long expiration;
    private Set<String> ownerIds;
    private Set<Account> owners;
    private Integer freeCount;




    public Set<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<String> privileges) {
        this.privileges = privileges;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Set<String> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(Set<String> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public Set<Account> getOwners() {
        return owners;
    }

    public void setOwners(Set<Account> owners) {
        this.owners = owners;
    }

    public Integer getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Integer freeCount) {
        this.freeCount = freeCount;
    }
}

package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.helpers.Tokens;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@javax.persistence.Entity
public class Licence extends Entity {

    @ManyToOne
    private Account owner;

    @ElementCollection
    private final Set<String> privileges = new HashSet<>();

    @Column
    private String activationCode = Tokens.randomLicenceCode();

    @Column(nullable = false)
    private Long expiration = 0L;




    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
        setLastModified(System.currentTimeMillis());
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void addPrivileges(Collection<String> privileges) {
        this.privileges.addAll(privileges);
        setLastModified(System.currentTimeMillis());
    }

    public String getActivationCode() {
        return activationCode;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
        setLastModified(System.currentTimeMillis());
    }

    public void activate(Account owner) {
        if (this.owner != null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "licence not found");
        this.owner = owner;
        owner.addLicence(this);
        if (expiration <= 0) expiration = Long.MAX_VALUE;
        else expiration += System.currentTimeMillis();
    }

    public boolean isValid() {
        return expiration > System.currentTimeMillis();
    }

    @Override
    public EntityType getType() {
        return EntityType.Licence;
    }
}

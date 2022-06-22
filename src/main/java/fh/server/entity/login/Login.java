package fh.server.entity.login;

import fh.server.constant.LoginMethod;
import fh.server.entity.Account;

import javax.persistence.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Login extends fh.server.entity.Entity {

    @ManyToOne
    private Account owner;

    @Column(nullable = false)
    private String identifier;




    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
        setLastModified(System.currentTimeMillis());
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        setLastModified(System.currentTimeMillis());
    }

    public abstract LoginMethod getLoginMethod();
}

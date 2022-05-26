package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.entity.login.Login;
import fh.server.helpers.Tokens;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Account extends Entity {

    @Column
    private String token = Tokens.randomAccountToken();

    @ManyToMany
    private final Set<Login> logins = new HashSet<>();




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Login> getLogins() {
        return logins;
    }

    public void addLogin(Login login) {
        if (!getId().equals(login.getOwnerId())) throw new IllegalStateException();
        logins.add(login);
        setLastModified(System.currentTimeMillis());
    }

    public void removeLogin(Login login) {
        logins.remove(login);
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public EntityType getType() {
        return EntityType.Account;
    }

    @Override
    public String toString() {
        return String.format("account %s", getId());
    }
}

package fh.server.entity;

import fh.server.entity.login.Login;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@javax.persistence.Entity
public class Account extends Entity {

    @ManyToMany
    private final Set<Login> logins = new HashSet<>();




    public Set<Login> getLogins() {
        return logins;
    }

    public void addLogin(Login login) {
        if (!getId().equals(login.getAccountId())) throw new IllegalStateException();
        logins.add(login);
        setLastModified(System.currentTimeMillis());
    }

    public void removeLogin(Login login) {
        logins.remove(login);
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return String.format("account %s", getId());
    }
}

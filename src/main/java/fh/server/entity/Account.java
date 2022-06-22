package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.context.Principal;
import fh.server.entity.login.Login;
import fh.server.helpers.Tokens;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Account extends Entity implements Principal {

    @Column(nullable = false)
    private String name;

    @Column
    private String token = Tokens.randomAccountToken();

//    @Column
//    private String globalRole; // todo identify super admin here instead of special token

    @OneToMany
    private final Set<Login> logins = new HashSet<>();

    @OneToMany
    private final Set<Licence> licences = new HashSet<>();

//    @OneToMany
//    private final Set<Resource> resources = new HashSet<>(); // todo

//    @OneToMany
//    private final Set<Resource> inbox = new HashSet<>(); // todo




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastModified(System.currentTimeMillis());
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
        setLastModified(System.currentTimeMillis());
    }

    public Set<Login> getLogins() {
        return logins;
    }


    public void addLogin(Login login) {
        if (!equals(login.getOwner())) throw new IllegalStateException();
        logins.add(login);
        setLastModified(System.currentTimeMillis());
    }

    public void removeLogin(Login login) {
        logins.remove(login);
        setLastModified(System.currentTimeMillis());
    }

    public Set<Licence> getLicences() {
        return licences;
    }

    public void addLicence(Licence licence) {
        if (!equals(licence.getOwner())) throw new IllegalStateException();
        licences.add(licence);
        setLastModified(System.currentTimeMillis());
    }

    public void removeLicence(Licence login) {
        licences.remove(login);
        setLastModified(System.currentTimeMillis());
    }

    public boolean hasLicence(String privilege) {
        for (Licence licence : licences) if (licence.isValid() && licence.getPrivileges().contains(privilege)) return true;
        return false;
    }

    @Override
    public EntityType getType() {
        return EntityType.Account;
    }

    @Override
    public String toString() {
        return String.format("account %s", getId());
    }

    @Override
    public boolean hasAlias(Resource resource) {
        return resource.hasAlias(this);
    }

    @Override
    public boolean hasAccount() {
        return true;
    }

    @Override
    public Alias getAlias(Resource resource) {
        return resource.getAlias(this);
    }

    @Override
    public Account getAccount() {
        return this;
    }
}

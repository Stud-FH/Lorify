package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.context.Principal;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
public class Alias extends Resource implements Principal {

    @ManyToOne
    private Account account;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private final Set<Submission> submissions = new java.util.LinkedHashSet<>();



    public void setAccount(Account account) {
        this.account = account;
        setLastModified(System.currentTimeMillis());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setLastModified(System.currentTimeMillis());
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        setLastModified(System.currentTimeMillis());
    }

    public void removeSubmission(Submission submission) {
        submissions.remove(submission);
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public EntityType getType() {
        return EntityType.Alias;
    }

    @Override
    public boolean hasAlias(Resource resource) {
        return resource.inherits(getScope());
    }

    @Override
    public boolean hasAccount() {
        return account != null;
    }

    @Override
    public Alias getAlias(Resource resource) {
        return resource.inherits(getScope())? this : null;
    }

    @Override
    public Account getAccount() {
        return account;
    }
}

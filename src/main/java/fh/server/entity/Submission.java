package fh.server.entity;

import fh.server.constant.EntityType;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@javax.persistence.Entity
public class Submission extends Entity {

    @ManyToOne
    private Alias alias;

    @ManyToOne
    @JoinColumn
    private Poll poll;

    @Column(nullable = false)
    private String string;




    public Alias getAlias() {
        return alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
        setLastModified(System.currentTimeMillis());
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
        setLastModified(System.currentTimeMillis());
    }

    public String getString() {
        return string;
    }

    public void setString(String submission) {
        this.string = submission;
        setLastModified(System.currentTimeMillis());
    }

    public void deploy() {
        poll.addSubmission(this);
        alias.addSubmission(this);
    }

    public void remove() {
        poll.removeSubmission(this);
        alias.removeSubmission(this);
    }

    @Override
    public EntityType getType() {
        return EntityType.Submission;
    }
}

package fh.server.entity.login;

import fh.server.constant.LoginMethod;

import javax.persistence.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Login extends fh.server.entity.Entity {

    @Column
    private String accountId;




    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
        setLastModified(System.currentTimeMillis());
    }

    public abstract LoginMethod getLoginMethod();
}

package fh.server.entity.login;

import fh.server.constant.LoginMethod;

import javax.persistence.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Login extends fh.server.entity.Entity {

    public abstract LoginMethod getLoginMethod();
}

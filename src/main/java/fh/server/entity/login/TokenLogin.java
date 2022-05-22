package fh.server.entity.login;

import fh.server.constant.LoginMethod;

import javax.persistence.*;
import java.util.UUID;


@javax.persistence.Entity
public class TokenLogin extends Login {

    @Column
    private String token = UUID.randomUUID().toString();




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public LoginMethod getLoginMethod() {
        return LoginMethod.Token;
    }
}

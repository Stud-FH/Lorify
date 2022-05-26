package fh.server.rest.dto;

import fh.server.entity.login.Login;
import fh.server.rest.mapper.DTOMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO extends EntityDTO {


    private String token;

    private Set<LoginDTO> logins;




    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<LoginDTO> getLogins() {
        return logins;
    }

    public void setLogins(Set<Login> logins) {
        this.logins = logins.stream().map(DTOMapper.INSTANCE::map).collect(Collectors.toSet());
    }
}
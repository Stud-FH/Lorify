package fh.server.rest.dto;

import fh.server.entity.login.Login;

public class LoginDTO extends EntityDTO {

    private String ownerId;
    private String identifier;

    public LoginDTO(Login source) {
        super(source);
        ownerId = source.getOwner().getId();
        identifier = source.getIdentifier();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getIdentifier() {
        return identifier;
    }
}

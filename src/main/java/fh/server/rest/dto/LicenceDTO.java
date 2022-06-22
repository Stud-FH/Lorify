package fh.server.rest.dto;

import fh.server.entity.Licence;

import java.util.Set;

public class LicenceDTO extends EntityDTO {

    private String ownerId;
    private Set<String> privileges;
    private String activationCode;
    private Long expiration;

    public LicenceDTO(Licence source) {
        super(source);
        ownerId = source.getOwner().getId();
        privileges = source.getPrivileges();
        activationCode = source.getActivationCode(); //todo pruning?
        expiration = source.getExpiration();
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public Long getExpiration() {
        return expiration;
    }
}

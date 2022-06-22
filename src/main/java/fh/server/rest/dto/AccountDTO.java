package fh.server.rest.dto;

import fh.server.context.Principal;
import fh.server.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class AccountDTO extends EntityDTO {


    private String name;
    private String token;
    private Set<LoginDTO> logins;
    private Set<LicenceDTO> licences;

    public AccountDTO(Account source, Principal principal) {
        super(source);
        name = source.getName();
        if (source.equals(principal)) {
            token = source.getToken();
            logins = source.getLogins().stream().map(LoginDTO::new).collect(Collectors.toSet());
            licences = source.getLicences().stream().map(LicenceDTO::new).collect(Collectors.toSet());
        }
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public Set<LoginDTO> getLogins() {
        return logins;
    }

    public Set<LicenceDTO> getLicences() {
        return licences;
    }
}
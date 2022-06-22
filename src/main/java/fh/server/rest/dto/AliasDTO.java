package fh.server.rest.dto;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Alias;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AliasDTO extends ResourceDTO {

    private String accountId;

    private String token;

    public AliasDTO(Alias source, Principal principal) {
        super(source, principal);
        if (ps.getResource().meets(Permission.UserView)) {
            accountId = source.getAccount().getId();
        }
        if (ps.getResource().meets(Permission.ExclusiveView)) {
            token = source.getToken();
        }
    }

    public String getAccountId() {
        return accountId;
    }

    public String getToken() {
        return token;
    }
}

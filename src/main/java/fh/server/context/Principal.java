package fh.server.context;

import fh.server.constant.Permission;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface Principal {

    String getId();

    boolean hasAlias(Resource resource);
    boolean hasAccount();
    Alias getAlias(Resource resource);
    Account getAccount();

    default void requireAccount() {
        if (!hasAccount())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "account required");
    }

    default void requireAlias(Resource resource) {
        if (!hasAlias(resource))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "alias required");
    }

    default void requirePermission(Resource resource, Permission required) {
        if (resource.getPermission(this).fails(required))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "account required");
    }

    default void requireSuperAdmin() {
        if (!getAccount().getToken().startsWith("ADMIN-"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "super admin account required");
    }

}

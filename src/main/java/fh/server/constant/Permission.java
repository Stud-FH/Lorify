package fh.server.constant;

import java.security.AccessControlException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Permission {
    None,
    UserView(None),
    AuthorView(None, UserView),
    AdminView(None, UserView, AuthorView),
    ExclusiveView(None, UserView, AuthorView, AdminView),
    UserAccess(None, UserView),
    AuthorAccess(None, UserView, AuthorView, UserAccess),
    AdminAccess(None, UserView, AuthorView, AdminView, UserAccess, AuthorAccess),
    ExclusiveAccess(None, UserView, AuthorView, AdminView, ExclusiveView, UserAccess, AuthorAccess, AdminAccess, null);

    private final Set<Permission> children = new HashSet<>();
    Permission(Permission... children) {
        this.children.addAll(Arrays.asList(children));
    }

    public boolean fails(Permission required) {
        return this != required && !children.contains(required);
    }

    public boolean meets(Permission required) {
        return this == required || children.contains(required);
    }

    public void require(Permission required) {
        if (fails(required)) throw new AccessControlException(String.format("access denied: permission %s required", required));
    }

}

package fh.server.constant;

public enum AliasManagementPolicy {
    UserOnly(true, false),
    AdminOrUser(true, true),
    AdminOnly(false, true)
    ;

    private final boolean approveUser, approveAdmin;

    AliasManagementPolicy(boolean approveUser, boolean approveAdmin) {
        this.approveUser = approveUser;
        this.approveAdmin = approveAdmin;
    }

    public boolean approvesAdmin() {
        return approveAdmin;
    }

    public boolean approvesUser() {
        return approveUser;
    }
}

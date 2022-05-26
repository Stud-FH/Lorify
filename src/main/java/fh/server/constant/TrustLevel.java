package fh.server.constant;

public enum TrustLevel {
    Owner, Operator, Viewer, Excluded;


    public boolean meets(TrustLevel requirement) {

        switch (this) {
            case Owner: return true;
            case Operator: return requirement == Excluded || requirement == Viewer || requirement == Operator;
            case Viewer: return requirement == Excluded || requirement == Viewer;
            case Excluded:
            default: return false;
        }
    }
}

package fh.server.helpers.interpreter;

import fh.server.constant.TrustLevel;

public interface G extends Generic<TrustLevel> {

    static G ownership() {
        return context -> context.getVictim().getOwnerId().equals(context.getPrincipal().getId())? TrustLevel.Owner : null;
    }

    static G operator() {
        return context -> TrustLevel.Operator;
    }

    static G operatorIf(B arg0) {
        return context -> {
            try {
                return arg0.resolve(context)? TrustLevel.Operator : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

    static G operatorUnless(B arg0) {
        return context -> {
            try {
                return !arg0.resolve(context)? TrustLevel.Operator : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

    static G viewer() {
        return context -> TrustLevel.Viewer;
    }

    static G viewerIf(B arg0) {
        return context -> {
            try {
                return arg0.resolve(context)? TrustLevel.Viewer : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

    static G viewerUnless(B arg0) {
        return context -> {
            try {
                return !arg0.resolve(context)? TrustLevel.Viewer : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

    static G excluded() {
        return context -> TrustLevel.Excluded;
    }

    static G excludedIf(B arg0) {
        return context -> {
            try {
                return arg0.resolve(context)? TrustLevel.Excluded : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

    static G excludedUnless(B arg0) {
        return context -> {
            try {
                return !arg0.resolve(context)? TrustLevel.Excluded : null;
            } catch (Exception e) {
                return null;
            }
        };
    }

}

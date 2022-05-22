package fh.server.helpers.interpreter;

import fh.server.helpers.Context;

@FunctionalInterface
public interface Generic <T> {
    
    T resolve(Context context);
}

package fh.server.helpers.interpreter;

import fh.server.helpers.Operation;

@FunctionalInterface
public interface Generic <T> {
    
    T resolve(Operation operation);

}

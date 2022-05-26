package fh.server.helpers.interpreter;

import fh.server.helpers.Operation;

import java.util.stream.Stream;

@FunctionalInterface
public interface GenericStream<T> {

    Stream<T> resolve(Operation operation);
}

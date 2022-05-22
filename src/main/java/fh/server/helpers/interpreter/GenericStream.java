package fh.server.helpers.interpreter;

import fh.server.helpers.Context;

import java.util.stream.Stream;

@FunctionalInterface
public interface GenericStream<T> {

    Stream<T> resolve(Context context);
}

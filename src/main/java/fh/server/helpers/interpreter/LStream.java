package fh.server.helpers.interpreter;

import fh.server.helpers.Context;

import java.util.Iterator;
import java.util.stream.Stream;

@FunctionalInterface
public interface LStream extends GenericStream<Long> {

    Stream<Long> resolve(Context context);

    static LStream convert(BStream arg0) {
        return context -> arg0.resolve(context).map(b -> b? 1L : 0L);
    }

    static LStream convert(SStream arg0) {
        return context -> arg0.resolve(context).map(Long::parseLong);
    }

    static LStream convert(IStream arg0) {
        return context -> arg0.resolve(context).map(Integer::longValue);
    }

    static LStream convert(DStream arg0) {
        return context -> arg0.resolve(context).map(Double::longValue);
    }

    static LStream distinct(LStream arg0) {
        return context -> arg0.resolve(context).distinct();
    }

    static LStream add(LStream arg0, LStream arg1) {
        return context -> {
            Iterator<Long> i = arg1.resolve(context).iterator();
            return arg0.resolve(context).map(l -> i.hasNext()? l + i.next() : l);
        };
    }

    static LStream scale(L arg0, LStream arg1) {
        return context -> {
            Long scalar = arg0.resolve(context);
            return arg1.resolve(context).map(l -> scalar * l);
        };
    }
}

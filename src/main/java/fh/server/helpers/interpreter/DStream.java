package fh.server.helpers.interpreter;

import fh.server.helpers.Context;

import java.util.Iterator;
import java.util.stream.Stream;

@FunctionalInterface
public interface DStream extends GenericStream<Double> {
    
    Stream<Double> resolve(Context context);

    static DStream convert(BStream arg0) {
        return context -> arg0.resolve(context).map(b -> b? 1.0 : 0.0);
    }

    static DStream convert(SStream arg0) {
        return context -> arg0.resolve(context).map(Double::parseDouble);
    }

    static DStream convert(IStream arg0) {
        return context -> arg0.resolve(context).map(Integer::doubleValue);
    }

    static DStream convert(LStream arg0) {
        return context -> arg0.resolve(context).map(Long::doubleValue);
    }

    static DStream distinct(DStream arg0) {
        return context -> arg0.resolve(context).distinct();
    }

    static DStream add(DStream arg0, DStream arg1) {
        return context -> {
            Iterator<Double> i = arg1.resolve(context).iterator();
            return arg0.resolve(context).map(l -> i.hasNext()? l + i.next() : l);
        };
    }

    static DStream scale(D arg0, DStream arg1) {
        return context -> {
            Double scalar = arg0.resolve(context);
            return arg1.resolve(context).map(l -> scalar * l);
        };
    }

}

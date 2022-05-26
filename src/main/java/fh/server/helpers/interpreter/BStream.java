package fh.server.helpers.interpreter;

@FunctionalInterface
public interface BStream extends GenericStream<Boolean> {

    static BStream convert(SStream arg0) {
        return context -> arg0.resolve(context).map(Boolean::parseBoolean);
    }

    static BStream convert(IStream arg0) {
        return context -> arg0.resolve(context).map(i -> i.equals(0));
    }

    static BStream convert(DStream arg0) {
        return context -> arg0.resolve(context).map(d -> d < -1e-6 || d > 1e-6);
    }

    static BStream convert(LStream arg0) {
        return context -> arg0.resolve(context).map(l -> l.equals(0L));
    }

    static BStream invert(BStream arg0) {
        return context -> arg0.resolve(context).map(b -> !b);
    }
}

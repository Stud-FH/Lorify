package fh.server.helpers.interpreter;

@FunctionalInterface
public interface D extends Generic<Double> {

    static D convert(S arg0) {
        return context -> Double.parseDouble(arg0.resolve(context));
    }

    static D convert(I arg0) {
        return context -> arg0.resolve(context).doubleValue();
    }

    static D convert(L arg0) {
        return context -> arg0.resolve(context).doubleValue();
    }

    static D convert(B arg0) {
        return context -> arg0.resolve(context) ? 1.0 : 0.0;
    }

    static D abs(D arg0) {
        return context -> Math.abs(arg0.resolve(context));
    }

    static D neg(D arg0) {
        return context -> -arg0.resolve(context);
    }

    static D sum(DStream arg0) {
        return context -> arg0.resolve(context).mapToDouble(f -> f).sum();
    }

    static D prod(DStream arg0) {
        return context -> arg0.resolve(context).mapToDouble(f -> f).reduce(1, (a,b) -> a*b);
    }

    static D min(DStream arg0) {
        return context -> arg0.resolve(context).mapToDouble(f -> f).min().orElseThrow(IllegalStateException::new);
    }

    static D max(DStream arg0) {
        return context -> arg0.resolve(context).mapToDouble(f -> f).max().orElseThrow(IllegalStateException::new);
    }

    static D avg(IStream arg0) {
        return context -> arg0.resolve(context).mapToInt(f -> f).average().orElseThrow(IllegalStateException::new);
    }

    static D avg(DStream arg0) {
        return context -> arg0.resolve(context).mapToDouble(f -> f).average().orElseThrow(IllegalStateException::new);
    }

    static D avg(LStream arg0) {
        return context -> arg0.resolve(context).mapToLong(f -> f).average().orElseThrow(IllegalStateException::new);
    }

    static D norm(IStream arg0) {
        return context -> Math.sqrt(arg0.resolve(context).mapToDouble(f -> f*f).sum());
    }

    static D norm(DStream arg0) {
        return context -> Math.sqrt(arg0.resolve(context).mapToDouble(f -> f*f).sum());
    }

    static D norm(LStream arg0) {
        return context -> Math.sqrt(arg0.resolve(context).mapToDouble(f -> f*f).sum());
    }
}

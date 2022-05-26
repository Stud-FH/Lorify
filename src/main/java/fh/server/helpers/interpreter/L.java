package fh.server.helpers.interpreter;

@FunctionalInterface
public interface L extends Generic<Long> {

    static L convert(B arg0) {
        return context -> arg0.resolve(context) ? 1L : 0L;
    }

    static L convert(S arg0) {
        return context -> Long.parseLong(arg0.resolve(context));
    }

    static L convert(I arg0) {
        return context -> arg0.resolve(context).longValue();
    }

    static L convert(D arg0) {
        return context -> arg0.resolve(context).longValue();
    }

    static <T> L count(GenericStream<T> arg0) {
        return context -> arg0.resolve(context).count();
    }
}

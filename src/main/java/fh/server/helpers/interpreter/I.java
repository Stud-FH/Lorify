package fh.server.helpers.interpreter;

import fh.server.entity.widget.Poll;

@FunctionalInterface
public interface I extends Generic<Integer> {

    static I convert(S arg0) {
        return context -> Integer.parseInt(arg0.resolve(context));
    }

    static I convert(D arg0) {
        return context -> arg0.resolve(context).intValue();
    }

    static I convert(L arg0) {
        return context -> arg0.resolve(context).intValue();
    }

    static I convert(B arg0) {
        return context -> arg0.resolve(context) ? 1 : 0;
    }

    static I abs(I arg0) {
        return context -> Math.abs(arg0.resolve(context));
    }

    static I neg(I arg0) {
        return context -> -arg0.resolve(context);
    }

    static I sum(IStream arg0) {
        return context -> arg0.resolve(context).mapToInt(f -> f).sum();
    }

    static I prod(IStream arg0) {
        return context -> arg0.resolve(context).mapToInt(f -> f).reduce(1, (a,b) -> a*b);
    }

    static I min(IStream arg0) {
        return context -> arg0.resolve(context).mapToInt(f -> f).min().orElseThrow(IllegalStateException::new);
    }

    static I max(IStream arg0) {
        return context -> arg0.resolve(context).mapToInt(f -> f).max().orElseThrow(IllegalStateException::new);
    }

    static I length(S arg0) {
        return context -> arg0.resolve(context).length();
    }

    static <T> I count(GenericStream<T> arg0) {
        return context -> Math.toIntExact(arg0.resolve(context).count());
    }

    static I pollLoad() {
        return context -> {
            Poll poll = context.victimAsPoll();
            return poll.getSubmissions().values().stream().mapToInt(poll::getQuantification).sum();
        };
    }

    static I pollQuantification(S arg0) {
        return context -> context.victimAsPoll().getQuantification(arg0.resolve(context));
    }
}

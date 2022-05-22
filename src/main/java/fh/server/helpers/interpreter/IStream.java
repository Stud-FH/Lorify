package fh.server.helpers.interpreter;

import fh.server.entity.widget.Poll;
import fh.server.helpers.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

@FunctionalInterface
public interface IStream extends GenericStream<Integer> {

    Stream<Integer> resolve(Context context);


    static IStream convert(BStream arg0) {
        return context -> arg0.resolve(context).map(b -> b? 1 : 0);
    }

    static IStream convert(SStream arg0) {
        return context -> arg0.resolve(context).map(Integer::parseInt);
    }

    static IStream convert(DStream arg0) {
        return context -> arg0.resolve(context).map(Double::intValue);
    }

    static IStream convert(LStream arg0) {
        return context -> arg0.resolve(context).map(Long::intValue);
    }

    static IStream distinct(IStream arg0) {
        return context -> arg0.resolve(context).distinct();
    }

    static IStream rangeExclusive(I arg0, I arg1) {
        return context -> new ArrayList<Integer>(){{
            int min = arg0.resolve(context);
            int max = arg1.resolve(context);
            for (int i = min; i < max; i++) add(i);
        }}.stream();
    }

    static IStream rangeInclusive(I arg0, I arg1) {
        return context -> new ArrayList<Integer>(){{
            int min = arg0.resolve(context);
            int max = arg1.resolve(context);
            for (int i = min; i <= max; i++) add(i);
        }}.stream();
    }

    static IStream add(IStream arg0, IStream arg1) {
        return context -> {
            Iterator<Integer> i = arg1.resolve(context).iterator();
            return arg0.resolve(context).map(l -> i.hasNext()? l + i.next() : l);
        };
    }

    static IStream scale(I arg0, IStream arg1) {
        return context -> {
            Integer scalar = arg0.resolve(context);
            return arg1.resolve(context).map(l -> scalar * l);
        };
    }

    static IStream pollSubmissionQuantification() {
        return context -> {
            Poll poll = context.getPoll();
            return poll.getSubmissions().values().stream().map(poll::getQuantification);
        };
    }

    static IStream pollQuantification(SStream arg0) {
        return context -> arg0.resolve(context).map(context.getPoll()::getQuantification);
    }
}

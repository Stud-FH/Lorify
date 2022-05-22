package fh.server.helpers.interpreter;

import fh.server.entity.widget.Poll;
import fh.server.helpers.Context;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@FunctionalInterface
public interface SStream extends GenericStream<String> {

    Stream<String> resolve(Context context);

    static <T> SStream convert(GenericStream<T> arg0) {
        return context -> arg0.resolve(context).map(T::toString);
    }

    static SStream distinct(SStream arg0) {
        return context -> arg0.resolve(context).distinct();
    }

    static SStream pollQuantificationKeys() {
        return context -> context.getPoll().getQuantification().keySet().stream();
    }

    static SStream split(S arg0, S arg1) {
        return context -> Arrays.stream(arg0.resolve(context).split(Pattern.quote(arg1.resolve(context))));
    }

    static SStream splitRegex(S arg0, S arg1) {
        return context -> Arrays.stream(arg0.resolve(context).split(arg1.resolve(context)));
    }

    static SStream inputMc() {
        return context -> Arrays.stream(context.getInput().split(Poll.MC_SEPARATOR));
    }
}

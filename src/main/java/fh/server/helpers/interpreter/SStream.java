package fh.server.helpers.interpreter;

import fh.server.entity.widget.Poll;

import java.util.Arrays;
import java.util.regex.Pattern;

@FunctionalInterface
public interface SStream extends GenericStream<String> {

    static <T> SStream convert(GenericStream<T> arg0) {
        return context -> arg0.resolve(context).map(T::toString);
    }

    static SStream distinct(SStream arg0) {
        return context -> arg0.resolve(context).distinct();
    }

    static SStream pollQuantificationKeys() {
        return context -> context.victimAsPoll().getQuantification().keySet().stream();
    }

    static SStream split(S arg0, S arg1) {
        return context -> Arrays.stream(arg0.resolve(context).split(Pattern.quote(arg1.resolve(context))));
    }

    static SStream splitRegex(S arg0, S arg1) {
        return context -> Arrays.stream(arg0.resolve(context).split(arg1.resolve(context)));
    }

    static SStream inputMc() {
        return context -> Arrays.stream(context.getValue().split(Poll.MC_SEPARATOR));
    }
}

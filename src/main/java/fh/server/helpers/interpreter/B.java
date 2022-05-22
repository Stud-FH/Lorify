package fh.server.helpers.interpreter;

import fh.server.entity.widget.Poll;
import fh.server.helpers.Context;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@FunctionalInterface
public interface B extends Generic<Boolean> {
    
    Boolean resolve(Context context);

    static B convert(S arg0) {
        return context -> Boolean.parseBoolean(arg0.resolve(context));
    }

    static B convert(I arg0) {
        return context -> !arg0.resolve(context).equals(0);
    }

    static B convert(D arg0) {
        return context -> {
            double d = arg0.resolve(context);
            return d < -1e-6 || d > 1e-6;
        };
    }

    static B convert(L arg0) {
        return context -> !arg0.resolve(context).equals(0L);
    }

    static B not(B arg0) {
        return context -> !arg0.resolve(context);
    }

    static B and(B arg0, B b1) {
        return context -> arg0.resolve(context) && b1.resolve(context);
    }

    static B or(B arg0, B b1) {
        return context -> arg0.resolve(context) || b1.resolve(context);
    }

    static B nand(B arg0, B b1) {
        return context -> !arg0.resolve(context) || !b1.resolve(context);
    }

    static B nor(B arg0, B b1) {
        return context -> !arg0.resolve(context) && !b1.resolve(context);
    }

    static B xor(B arg0, B arg1) {
        return context -> arg0.resolve(context) ^ arg1.resolve(context);
    }

    static B xnor(B arg0, B arg1) {
        return context -> arg0.resolve(context) == arg1.resolve(context);
    }

    static B all(BStream arg0) {
        return context -> arg0.resolve(context).allMatch(c -> c);
    }

    static B either(BStream arg0) {
        return context -> arg0.resolve(context).anyMatch(c -> c);
    }

    static B none(BStream arg0) {
        return context -> arg0.resolve(context).noneMatch(c -> c);
    }

    static B uniform(BStream arg0) {
        return context -> arg0.resolve(context).distinct().count() <= 1;
    }

    static B artifactTag(S arg0) {
        return context -> context.getArtifact().hasTag(arg0.resolve(context));
    }

    static B aliasTag(S arg0) {
        return context -> context.getArtifact().hasTag(arg0.resolve(context));
    }

    static B pollHasQuantification(S arg0) {
        return context -> context.getPoll().hasQuantification(arg0.resolve(context));
    }

    static B pollOption() {
        return context -> context.getPoll().hasQuantification(context.getInput());
    }

    static B pollOptionMc() {
        return context -> Arrays.stream(context.getInput().split(Poll.MC_SEPARATOR)).allMatch(context.getPoll()::hasQuantification);
    }

    static B pollLimit() {
        return context -> {
            Poll poll = context.getPoll();
            int load = poll.getSubmissions().values().stream().mapToInt(poll::getQuantification).sum();
            int additional = context.getPoll().getQuantification(context.getInput());
            int limit = poll.getQuantification(Poll.KEY_LIMIT);
            return load + additional <= limit;
        };
    }

    static B pollLimitMc() {
        return context -> {
            Poll poll = context.getPoll();
            int load = poll.getSubmissions().values().stream().mapToInt(s -> Arrays.stream(s.split(Poll.MC_SEPARATOR)).mapToInt(poll::getQuantification).sum()).sum();
            int additional = Arrays.stream(context.getInput().split(Poll.MC_SEPARATOR)).mapToInt(poll::getQuantification).sum();
            int limit = poll.getQuantification(Poll.KEY_LIMIT);
            return load + additional <= limit;
        };
    }

    static B artifactHasAttrib(S arg0) {
        return context -> context.getArtifact().hasAttribute(arg0.resolve(context));
    }

    static B principalHasAttrib(S arg0) {
        return context -> context.getPrincipal().hasAttribute(arg0.resolve(context));
    }

    static B aliasHasAttrib(S arg0) {
        return context -> context.getAlias().hasAttribute(arg0.resolve(context));
    }

    static <T> B listDistinct(GenericStream<T> arg0) {
        return context -> {
            Set<T> set = new HashSet<>();
            return arg0.resolve(context).allMatch(set::add);
        };
    }

    static <T> B containsEither(GenericStream<T> arg0, GenericStream<T> arg1) {
        return context -> {
            Set<T> master = arg0.resolve(context).collect(Collectors.toSet());
            return arg1.resolve(context).anyMatch(master::contains);
        };
    }

    static <T> B containsAll(GenericStream<T> arg0, GenericStream<T> arg1) {
        return context -> {
            Set<T> master = arg0.resolve(context).collect(Collectors.toSet());
            return arg1.resolve(context).allMatch(master::contains);
        };
    }

    static <T> B containsNone(GenericStream<T> arg0, GenericStream<T> arg1) {
        return context -> {
            Set<T> master = arg0.resolve(context).collect(Collectors.toSet());
            return arg1.resolve(context).noneMatch(master::contains);
        };
    }

    static <T> B compareAdjacent (GenericStream<T> arg0, BiPredicate<T, T> predicate) {
        return context -> {
            Iterator<T> iter = arg0.resolve(context).iterator();
            if (!iter.hasNext()) return true;
            T prev = iter.next();
            T next;
            while (iter.hasNext()) {
                next = iter.next();
                if (!predicate.test(prev, next)) return false;
                prev = next;
            }
            return true;
        };
    }

    static <T> B checkSingle (Generic<T> arg0, Predicate<T> predicate) {
        return context -> predicate.test(arg0.resolve(context));
    }

    static B evadeException(B arg0) {
        return context -> {
            try {
                return arg0.resolve(context);
            } catch (Exception e) {
                return false;
            }
        };
    }

    static B tautology() {
        return context -> true;
    }
}

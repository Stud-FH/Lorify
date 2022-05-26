package fh.server.helpers.interpreter;

import org.assertj.core.util.TriFunction;

import java.util.function.BiFunction;

@FunctionalInterface
public interface S extends Generic<String> {

    static <T> S convert(Generic<T> arg0) {
        return context -> arg0.resolve(context).toString();
    }

    static S victimAttribute(S arg0) {
        return context -> context.getVictim().getAttribute(arg0.resolve(context));
    }

    static S principalAttribute(S arg0) {
        return context -> context.getPrincipal().getAttribute(arg0.resolve(context));
    }

    static S modifyInt(I integer, S string, BiFunction<Integer, String, String> modification) {
        return context -> modification.apply(integer.resolve(context), string.resolve(context));
    }

    static S biModifyString(S string0, S string1, BiFunction<String, String, String> modification) {
        return context -> modification.apply(string0.resolve(context), string1.resolve(context));
    }

    static S triModifyString(S arg0, S arg1, S arg2, TriFunction<String, String, String, String> modification) {
        return context -> modification.apply(arg0.resolve(context), arg1.resolve(context), arg2.resolve(context));
    }

    static S concat(SStream arg0) {
        return context -> arg0.resolve(context).reduce("", (a,b) -> a+b);
    }
}

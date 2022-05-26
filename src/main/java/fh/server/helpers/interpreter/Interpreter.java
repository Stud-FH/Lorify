package fh.server.helpers.interpreter;

import fh.server.helpers.Operation;
import fh.server.constant.TrustLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Interpreter {

    public static B interpretB(String description) {
        return b(new Tokenizer(description));
    }

    public static BStream interpretBStream(String description) {
        return bs(new Tokenizer(description));
    }

    public static S interpretS(String description) {
        return s(new Tokenizer(description));
    }

    public static SStream interpretSStream(String description) {
        return ss(new Tokenizer(description));
    }

    public static I interpretI(String description) {
        return i(new Tokenizer(description));
    }

    public static IStream interpretIStream(String description) {
        return is(new Tokenizer(description));
    }

    public static D interpretD(String description) {
        return d(new Tokenizer(description));
    }

    public static DStream interpretDStream(String description) {
        return ds(new Tokenizer(description));
    }

    public static L interpretL(String description) {
        return l(new Tokenizer(description));
    }

    public static LStream interpretLStream(String description) {
        return ls(new Tokenizer(description));
    }

    public static GStream resilientGStream(String description) {
        if (description == null) return null;
        return gs(new Tokenizer(description));
    }

    public static B resilientB(String description, B ifNull) {
        return B.evadeException(description == null? ifNull : b(new Tokenizer(description)));
    }

    public static B resilientB(String description) {
        return description == null? B.positive() : B.evadeException(b(new Tokenizer(description)));
    }

    protected static G g(Tokenizer t) {
        if (t.tryConsume("'")) {
            TrustLevel constant = TrustLevel.valueOf(t.until("'"));
            return context -> constant;
        }
        G parsed = selectG(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static GStream gs(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<G> list = new ArrayList<>();
            do {
                list.add(g(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return context -> list.stream().map(f -> f.resolve(context));
        }
        GStream parsed = selectGStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static B b(Tokenizer t) {
        if (t.tryConsume("'")) {
            boolean constant = Boolean.parseBoolean(t.until("'"));
            return context -> constant;
        }
        B parsed = selectB(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static BStream bs(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<B> list = new ArrayList<>();
            do {
                list.add(b(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return  context -> list.stream().map(f -> f.resolve(context));
        }
        BStream parsed = selectBStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static S s(Tokenizer t) {
        if (t.tryConsume("'")) {
            String constant = t.until("'");
            return context -> constant;
        }
        S parsed = selectS(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static SStream ss(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<S> list = new ArrayList<>();
            do {
                list.add(s(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return context -> list.stream().map(f -> f.resolve(context));
        }
        SStream parsed = selectSStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static I i(Tokenizer t) {
        if (t.tryConsume("'")) {
            int constant = Integer.parseInt(t.until("'"));
            return context -> constant;
        }
        I parsed = selectI(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static IStream is(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<I> list = new ArrayList<>();
            do {
                list.add(i(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return context -> list.stream().map(f -> f.resolve(context));
        }
        IStream parsed = selectIStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static D d(Tokenizer t) {
        if (t.tryConsume("'")) {
            double constant = Double.parseDouble(t.until("'"));
            return context -> constant;
        }
        D parsed = selectD(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static DStream ds(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<D> list = new ArrayList<>();
            do {
                list.add(d(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return context -> list.stream().map(f -> f.resolve(context));
        }
        DStream parsed = selectDStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static L l(Tokenizer t) {
        if (t.tryConsume("'")) {
            long constant = Long.parseLong(t.until("'"));
            return context -> constant;
        }
        L parsed = selectL(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static LStream ls(Tokenizer t) {
        if (t.tryConsume("[")) {
            List<L> list = new ArrayList<>();
            do {
                list.add(l(t));
            } while (!t.tryConsume(", "));
            t.forceConsume("]");
            return context -> list.stream().map(f -> f.resolve(context));
        }
        LStream parsed = selectLStream(t.until("("), t);
        t.forceConsume(")");
        return parsed;
    }

    protected static G selectG(String selector, Tokenizer t) {
        switch(selector) {
            case "test-owner": return G.ownership();
            case "operator": return G.operator();
            case "operator-if": return G.operatorIf(b(t));
            case "operator-unless": return G.operatorUnless(b(t));
            case "viewer": return G.viewer();
            case "viewer-if": return G.viewerIf(b(t));
            case "viewer-unless": return G.viewerUnless(b(t));
            case "excluded": return G.excluded();
            case "excluded-if": return G.excludedIf(b(t));
            case "excluded-unless": return G.excludedUnless(b(t));

            default: throw new SyntaxError();
        }
    }

    protected static GStream selectGStream(String selector, Tokenizer t) {
        switch(selector) {

            default: throw new SyntaxError();
        }
    }

    protected static B selectB(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-s": return B.convert(s(t));
            case "convert-i": return B.convert(i(t));
            case "convert-d": return B.convert(d(t));
            case "convert-l": return B.convert(l(t));
            case "not": return B.not(b(t));
            case "and": return B.and(b(t), b(t));
            case "or": return B.or(b(t), b(t));
            case "nand": return B.nand(b(t), b(t));
            case "nor": return B.nor(b(t), b(t));
            case "xor": return B.xor(b(t), b(t));
            case "xnor": return B.xnor(b(t), b(t));
            case "all": return B.all(bs(t));
            case "either": return B.either(bs(t));
            case "none": return B.none(bs(t));
            case "uniform": return B.uniform(bs(t));
            case "victim-tag": return B.victimTag(s(t));
            case "principal-tag": return B.principalTag(s(t));
            case "poll-has-quantification": return B.pollHasQuantification(s(t));
            case "poll-option": return B.pollOption();
            case "poll-option-mc": return B.pollOptionMc();
            case "poll-limit": return B.pollLimit();
            case "poll-limit-mc": return B.pollLimitMc();
            case "victim-has-attribute": return B.victimHasAttribute(s(t));
            case "principal-has-attribute": return B.principalHasAttribute(s(t));
            case "i-equals": return B.compareAdjacent(is(t), Integer::equals);
            case "i-distinct": return B.listDistinct(is(t));
            case "d-equals": return B.compareAdjacent(ds(t), (a,b) -> Math.abs(a-b) < 1e-6);
            case "d-distinct": return B.listDistinct(ds(t));
            case "s-equals": return B.compareAdjacent(ss(t), String::equals);
            case "s-distinct": return B.listDistinct(ss(t));
            case "l-equals": return B.compareAdjacent(ls(t), Long::equals);
            case "l-distinct": return B.listDistinct(ls(t));
            case "s-contains": return B.compareAdjacent(ss(t), String::contains);
            case "s-prefix": return B.compareAdjacent(ss(t), String::startsWith);
            case "s-suffix": return B.compareAdjacent(ss(t), String::endsWith);
            case "s-null": return B.checkSingle(s(t), Objects::isNull);
            case "s-not-null": return B.checkSingle(s(t), Objects::nonNull);
            case "s-empty": return B.checkSingle(s(t), String::isEmpty);
            case "s-not-empty": return B.checkSingle(s(t), s -> !s.isEmpty());
            case "s-null-empty": return B.checkSingle(s(t), s -> s == null || s.isEmpty());
            case "s-not-null-empty": return B.checkSingle(s(t), s -> s != null && !s.isEmpty());
            case "i-smaller-strict": return B.compareAdjacent(is(t), (a, b) -> a < b);
            case "i-smaller-equals": return B.compareAdjacent(is(t), (a, b) -> a <= b);
            case "i-greater-strict": return B.compareAdjacent(is(t), (a, b) -> a > b);
            case "i-greater-equals": return B.compareAdjacent(is(t), (a, b) -> a >= b);
            case "d-smaller-strict": return B.compareAdjacent(ds(t), (a, b) -> a < b);
            case "d-smaller-equals": return B.compareAdjacent(ds(t), (a, b) -> a <= b);
            case "d-greater-strict": return B.compareAdjacent(ds(t), (a, b) -> a > b);
            case "d-greater-equals": return B.compareAdjacent(ds(t), (a, b) -> a >= b);
            case "l-smaller-strict": return B.compareAdjacent(ls(t), (a, b) -> a < b);
            case "l-smaller-equals": return B.compareAdjacent(ls(t), (a, b) -> a <= b);
            case "l-greater-strict": return B.compareAdjacent(ls(t), (a, b) -> a > b);
            case "l-greater-equals": return B.compareAdjacent(ls(t), (a, b) -> a >= b);
            case "s-contains-either": return B.containsEither(ss(t), ss(t));
            case "s-contains-all": return B.containsAll(ss(t), ss(t));
            case "s-contains-none": return B.containsNone(ss(t), ss(t));
            case "i-contains-either": return B.containsEither(is(t), is(t));
            case "i-contains-all": return B.containsAll(is(t), is(t));
            case "i-contains-none": return B.containsNone(is(t), is(t));
            case "d-contains-either": return B.containsEither(ds(t), ds(t));
            case "d-contains-all": return B.containsAll(ds(t), ds(t));
            case "d-contains-none": return B.containsNone(ds(t), ds(t));
            case "l-contains-either": return B.containsEither(ls(t), ls(t));
            case "l-contains-all": return B.containsAll(ls(t), ls(t));
            case "l-contains-none": return B.containsNone(ls(t), ls(t));

            default: throw new SyntaxError();
        }
    }

    protected static BStream selectBStream(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-s": return BStream.convert(ss(t));
            case "convert-i": return BStream.convert(is(t));
            case "convert-d": return BStream.convert(ds(t));
            case "convert-l": return BStream.convert(ls(t));
            case "invert": return BStream.invert(bs(t));

            default: throw new SyntaxError();
        }
    }

    protected static S selectS(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return S.convert(b(t));
            case "convert-i": return S.convert(i(t));
            case "convert-d": return S.convert(d(t));
            case "convert-l": return S.convert(l(t));
            case "operation": return Operation::getOperation;
            case "value": return Operation::getValue;
            case "previous": return Operation::getPrevious;
            case "alias-poll-submission": return context -> context.victimAsPoll().getSubmission(context.principalAsAlias());
            case "victim-attribute": return S.victimAttribute(s(t));
            case "principal-attribute": return S.principalAttribute(s(t));
            case "drop-first": return S.modifyInt(i(t), s(t), (i, s) -> s.substring(i));
            case "drop-last": return S.modifyInt(i(t), s(t), (i, s) -> s.substring(0, s.length() -i));
            case "keep-first": return S.modifyInt(i(t), s(t), (i, s) -> s.substring(0, i));
            case "keep-last": return S.modifyInt(i(t), s(t), (i, s) -> s.substring(s.length() -i));
            case "add-prefix": return S.biModifyString(s(t), s(t), (s0, s1) -> s0 + s1);
            case "add-suffix": return S.biModifyString(s(t), s(t), (s0, s1) -> s1 + s0);
            case "remove-prefix": return S.biModifyString(s(t), s(t), (s0, s1) -> s0.startsWith(s1)? s0.substring(s1.length()) : s0);
            case "remove-suffix": return S.biModifyString(s(t), s(t), (s0, s1) -> s0.endsWith(s1)? s0.substring(0, s0.length() - s1.length()) : s0);
            case "remove-first": return S.biModifyString(s(t), s(t), (s0, s1) -> s0.replaceFirst(s1, ""));
            case "remove-all": return S.biModifyString(s(t), s(t), (s0, s1) -> s0.replaceAll(s1, ""));
            case "replace-first": return S.triModifyString(s(t), s(t), s(t), String::replaceFirst);
            case "replace-all": return S.triModifyString(s(t), s(t), s(t), String::replaceAll);
            case "concat": return S.concat(ss(t));

            default: throw new SyntaxError();
        }
    }

    protected static SStream selectSStream(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return SStream.convert(bs(t));
            case "convert-i": return SStream.convert(is(t));
            case "convert-d": return SStream.convert(ds(t));
            case "convert-l": return SStream.convert(ls(t));
            case "distinct": return SStream.distinct(ss(t));
            case "poll-options":
            case "poll-quantification-keys": return SStream.pollQuantificationKeys();
            case "split": return SStream.split(s(t), s(t));
            case "split-regex": return SStream.splitRegex(s(t), s(t));
            case "input-mc": return SStream.inputMc();

            default: throw new SyntaxError();
        }
    }

    protected static I selectI(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return I.convert(b(t));
            case "convert-s": return I.convert(s(t));
            case "convert-d": return I.convert(d(t));
            case "convert-l": return I.convert(l(t));
            case "abs": return I.abs(i(t));
            case "neg": return I.neg(i(t));
            case "sum": return I.sum(is(t));
            case "prod": return I.prod(is(t));
            case "min": return I.min(is(t));
            case "max": return I.max(is(t));
            case "length": return I.length(s(t));
            case "count-b": return I.count(bs(t));
            case "count-s": return I.count(ss(t));
            case "count-i": return I.count(is(t));
            case "count-d": return I.count(ds(t));
            case "count-l": return I.count(ls(t));
            case "poll-load": return I.pollLoad();
            case "poll-quantification": return I.pollQuantification(s(t));
            default: throw new SyntaxError();
        }
    }

    protected static IStream selectIStream(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return IStream.convert(bs(t));
            case "convert-s": return IStream.convert(ss(t));
            case "convert-d": return IStream.convert(ds(t));
            case "convert-l": return IStream.convert(ls(t));
            case "distinct": return IStream.distinct(is(t));
            case "range-ex": return IStream.rangeExclusive(i(t), i(t));
            case "range-in": return IStream.rangeInclusive(i(t), i(t));
            case "scale": return IStream.scale(i(t), is(t));
            case "add": return IStream.add(is(t), is(t));
            case "poll-submission-quantification": return IStream.pollSubmissionQuantification();
            case "poll-quantification": return IStream.pollQuantification(ss(t));

            default: throw new SyntaxError();
        }
    }

    protected static D selectD(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-s": return D.convert(s(t));
            case "convert-i": return D.convert(i(t));
            case "convert-l": return D.convert(l(t));
            case "convert-b": return D.convert(b(t));
            case "abs": return D.abs(d(t));
            case "neg": return D.neg(d(t));
            case "sum": return D.sum(ds(t));
            case "prod": return D.prod(ds(t));
            case "min": return D.min(ds(t));
            case "max": return D.max(ds(t));
            case "avg-i": return D.avg(is(t));
            case "avg-d": return D.avg(ds(t));
            case "avg-l": return D.avg(ls(t));
            case "norm-i": return D.norm(is(t));
            case "norm-d": return D.norm(ds(t));
            case "norm-l": return D.norm(ls(t));
            default: throw new SyntaxError();
        }
    }

    protected static DStream selectDStream(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return DStream.convert(bs(t));
            case "convert-s": return DStream.convert(ss(t));
            case "convert-i": return DStream.convert(is(t));
            case "convert-l": return DStream.convert(ls(t));
            case "distinct": return DStream.distinct(ds(t));
            case "scale": return DStream.scale(d(t), ds(t));
            case "add": return DStream.add(ds(t), ds(t));

            default: throw new SyntaxError();
        }
    }

    protected static L selectL(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return L.convert(b(t));
            case "convert-s": return L.convert(s(t));
            case "convert-i": return L.convert(i(t));
            case "convert-d": return L.convert(d(t));
            case "count-b": return L.count(bs(t));
            case "count-s": return L.count(ss(t));
            case "count-i": return L.count(is(t));
            case "count-d": return L.count(ds(t));
            case "count-l": return L.count(ls(t));

            default: throw new SyntaxError();
        }
    }

    protected static LStream selectLStream(String selector, Tokenizer t) {
        switch(selector) {
            case "convert-b": return LStream.convert(bs(t));
            case "convert-s": return LStream.convert(ss(t));
            case "convert-i": return LStream.convert(is(t));
            case "convert-d": return LStream.convert(ds(t));
            case "distinct": return LStream.distinct(ls(t));
            case "scale": return LStream.scale(l(t), ls(t));
            case "add": return LStream.add(ls(t), ls(t));

            default: throw new SyntaxError();
        }
    }






}

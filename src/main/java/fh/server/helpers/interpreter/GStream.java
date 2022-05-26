package fh.server.helpers.interpreter;

import fh.server.helpers.Operation;
import fh.server.constant.TrustLevel;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@FunctionalInterface
public interface GStream extends GenericStream<TrustLevel> {

    default TrustLevel authenticate(Operation operation) {
        for (Iterator<TrustLevel> it = this.resolve(operation).iterator(); it.hasNext(); ) {
            TrustLevel cl = it.next();
            if (cl != null) return cl;
        }
        return null;
    }

    GStream defaultRuling = new GStream() {
        final List<G> rules = Arrays.asList(G.ownership(), G.viewer());

        @Override
        public Stream<TrustLevel> resolve(Operation operation) {
            return rules.stream().map(f -> f.resolve(operation));
        }
    };

}

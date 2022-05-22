package fh.server.helpers;

import java.util.HashMap;
import java.util.Map;

public class BinaryMapping {

    private static final Map<Character, Boolean> CHARACTER_BOOLEAN_MAP = new HashMap<Character, Boolean>() {{
        put('T', true);
        put('t', true);
        put('Y', true);
        put('y', true);
        put('F', false);
        put('f', false);
        put('N', false);
        put('n', false);
    }};

    public static boolean translate(char c) {
        Boolean b = CHARACTER_BOOLEAN_MAP.get(c);
        if (b == null) throw new IllegalArgumentException();
        return b;
    }

    public static char translate(boolean b) {
        return b? 't' : 'f';
    }

    public static boolean accepts(char c) {
        return CHARACTER_BOOLEAN_MAP.containsKey(c);
    }
}

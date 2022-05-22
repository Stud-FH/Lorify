package fh.server.helpers;

import java.util.Random;

public class Tokens {

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static final String digits = "0123456789";

    private static final char[] defaultCharPool = (alphabet.toLowerCase() + alphabet.toUpperCase() + digits).toCharArray();

    public static String random(int length, char... charPool) {
        if (length < 1) throw new IllegalArgumentException("token length required");
        if (charPool == null || charPool.length == 0) charPool = defaultCharPool;
        Random r = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            token.append(charPool[r.nextInt(charPool.length)]);
        }
        return token.toString();
    }
}

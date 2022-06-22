package fh.server.helpers;

import java.util.Random;
import java.util.UUID;

public class Tokens {

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static final String digits = "0123456789";

    private static final char[] defaultCharPool = (alphabet.toLowerCase() + alphabet.toUpperCase() + digits).toCharArray();
    private static final char[] uppercaseCharPool = (alphabet.toUpperCase() + digits).toCharArray();

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

    public static String randomAliasToken() {
        return "a-" + UUID.randomUUID();
    }

    public static String shortAliasToken() {
        return "a-" + random(4)+"-"+random(4);
    }

    public static String randomAccountToken() {
        return "A-" + UUID.randomUUID();
    }

    public static String randomLicenceCode() {
        return random(4, uppercaseCharPool)+"-"+random(4, uppercaseCharPool)+"-"+random(4, uppercaseCharPool)+"-"+random(4, uppercaseCharPool);
    }
}

package fh.server.helpers;

import java.util.HashSet;

public class CharPool extends HashSet<Character> {

    private static final String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uppercase = lowercase.toUpperCase();
    private static final String digits = "0123456789";
    private static final String separators = " .-_#@&+[]%$£¥¢";
    private static final String umlautLowercase = "äëïöüáâàéêèíîìóôòúûùçñ";
    private static final String umlautUppercase = umlautLowercase.toUpperCase();
    private static final String code = "`'\"/\\:;?!";



    public static CharPool of(char[] chars) {
        CharPool cp = new CharPool();
        for (char c : chars) cp.add(c);
        return cp;
    }
    public static CharPool of(String s) {
        return of(s.toCharArray());
    }

    public static CharPool lowercaseAlphabet() {
        return of(lowercase);
    }

    public static CharPool uppercaseAlphabet() {
        return of(uppercase);
    }

    public static CharPool digits() {
        return of(digits);
    }

    public static CharPool unsafe() {
        return of(code);
    }

    public static CharPool mixed() {
        return of(lowercase + uppercase + digits + separators + umlautLowercase + umlautUppercase);
    }

}

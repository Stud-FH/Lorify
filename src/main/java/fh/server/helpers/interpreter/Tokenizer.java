package fh.server.helpers.interpreter;

public class Tokenizer {

    private String string;

    public Tokenizer(String string) {
        this.string = string;
    }

    public String until(String expression) {
        int index = string.indexOf(expression);
        if (index < 0) throw new SyntaxError();
        String result = string.substring(0, index);
        string = string.substring(index + expression.length());
        return result;
    }

    public void forceConsume(String prefix) {
        if (!string.startsWith(prefix)) throw new SyntaxError();
        string = string.substring(prefix.length());
    }

    public void forceConsumeAny(String... prefixes) {
        for (String prefix : prefixes) {
            if (string.startsWith(prefix)) {
                string = string.substring(prefix.length());
            }
        }
        throw new SyntaxError();
    }

    public boolean tryConsume(String prefix) {
        if (!string.startsWith(prefix)) return false;
        string = string.substring(prefix.length());
        return true;
    }

    public boolean tryConsumeAny(String... prefixes) {
        for (String prefix : prefixes) {
            if (string.startsWith(prefix)) {
                string = string.substring(prefix.length());
                return true;
            }
        }
        return false;
    }

    public boolean hasNext(String prefix) {
        return string.startsWith(prefix);
    }
}

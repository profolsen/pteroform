package grammar;

import grammar.Terminal;

/**
 * Created by po917265 on 6/30/17.
 */
public class Token {
    private Terminal type;
    private String value;

    public Token(Terminal type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return value + ":" + type;
    }

    public Terminal type() {
        return type;
    }

    public String value() {
        return value;
    }
}

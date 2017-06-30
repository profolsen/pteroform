import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by po917265 on 6/29/17.
 */
public class Terminal implements Symbol{

    public static final Terminal epsilon = new Terminal("epsilon");
    public static final Terminal EOF = new Terminal("EOF");

    private Pattern pattern;
    private String name;
    private boolean phantom, ignore;

    public String match(String input) {
        return null;
    }

    private Terminal(String name) {  //used only to create the epsilon terminal.
        this.name = name;
        pattern = null;
        phantom = true;
        ignore = false;
    }

    public Terminal(String name, String pattern, boolean phantom, boolean ignore) {
        this.name = name;
        this.pattern = Pattern.compile(pattern);
        this.phantom = phantom;
        this.ignore = ignore;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object other) {
        if(!(other instanceof Terminal)) {
            return false;
        } else {
            return ((Terminal) other).name.equals(name); //tests for equality on name only.  Be careful!
        }
    }

    public HashSet<Terminal> first() {
        HashSet<Terminal> ans = new HashSet<Terminal>();
        ans.add(this);
        return ans;
    }

    @Override
    public boolean phantom() {
        return phantom;
    }

    public String toString() {
        if(this.equals(epsilon)) {
            return "epsilon";
        }
        return name;
    }

    public Token parse(String input) {
        if(this.equals(Terminal.EOF)) {
            return input.length() == 0 ? new Token(this, "") : null;
        } else if(this.equals(Terminal.epsilon)) {
            return new Token(this, "");
        }
        Matcher m = pattern.matcher(input);
        if(m.find()) {
            Token ans = new Token(this, m.group(0));
            return ans;
        } else {
            return null;
        }
    }

    public static Terminal keyword(String keyword, boolean phantom) {
        Terminal ans = new Terminal(keyword, "^\\Q" + keyword + "\\E", phantom, false);
        return ans;
    }

    public boolean ignore() {
        return ignore;
    }
}

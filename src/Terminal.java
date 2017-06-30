import java.util.HashSet;

/**
 * Created by po917265 on 6/29/17.
 */
public class Terminal implements Symbol{

    public static final Terminal epsilon = new Terminal("epsilon");
    public static final Terminal EOF = new Terminal("EOF");

    private String pattern;
    private String name;

    public String match(String input) {
        return null;
    }

    private Terminal(String name) {  //used only to create the epsilon terminal.
        this.name = name;
        pattern = null;
    }

    public Terminal(String name, String pattern) {
        this.name = name;
        this.pattern = pattern;
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

    public String toString() {
        if(this.equals(epsilon)) {
            return "epsilon";
        }
        return name + "[" + pattern + "]";
    }
}

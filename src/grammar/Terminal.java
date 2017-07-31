/*
MIT License

Copyright (c) 2017 Paul Olsen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package grammar;

import parsing.Token;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a Terminal.
 */
public class Terminal implements Symbol {

    /**
     * The terminal representing epsilon, the empty string.
     * This terminal is always a possible next terminal in any string.
     */
    public static final Terminal epsilon = new Terminal("epsilon");

    /**
     * The terminal representing EOF (end of file).
     * This terminal only matches the very end of a parse string.
     */
    public static final Terminal EOF = new Terminal("EOF");

    private Pattern pattern;
    private String name;
    private boolean phantom, ignore;

    //for the epsilon and eof terminals only.
    private Terminal(String name) {  //used only to create the epsilon terminal.
        this.name = name;
        pattern = null;
        phantom = true;
        ignore = false;
    }

    /**
     * Creates a Terminal.
     * @param name the name of the terminal.
     * @param pattern the pattern the terminal matches (a java regular expression).
     *                An '^' is automatically added to the pattern used here, since the pattern must match at the beginning of the input string.
     * @param phantom whether the terminal should be a phantom.
     * @param ignore when true, ignore all strings matching this terminal in the input.
     *               An example of ignore would typically be clearspace in most programming languages.
     *               Clearspace should be ignored and typically serves no structural purpose except for separating tokens in the input.
     *               Likewise, terminals for which ignore is true can only be used to separate meaningful tokens in the input.
     *               In all other cases they will be ignored (as if they were not present in the input).
     */
    public Terminal(String name, String pattern, boolean phantom, boolean ignore) {
        this.name = name;
        this.pattern = Pattern.compile("^" + pattern);
        this.phantom = phantom;
        this.ignore = ignore;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Terminal)) {
            return false;
        } else {
            return ((Terminal) other).name.equals(name); //tests for equality on name only.  Be careful!
        }
    }

    @Override
    public HashSet<Terminal> first() {
        HashSet<Terminal> ans = new HashSet<Terminal>();
        ans.add(this);
        return ans;
    }

    @Override
    public boolean phantom() {
        return phantom;
    }

    @Override
    public String toString() {
        if(this.equals(epsilon)) {
            return "epsilon";
        }
        return name;
    }

    /**
     * If this terminal matches the input, return a token storing this terminal and the part of the string that matched this terminal.
     * @param input some string.
     * @return a token or null if no match is possible.
     */
    public Token parse(String input, int lineNumber, int characterPosition) {
        if(this.equals(Terminal.EOF)) {
            return input.length() == 0 ? new Token(this, "", lineNumber, characterPosition) : null;
        } else if(this.equals(Terminal.epsilon)) {
            return new Token(this, "", lineNumber, characterPosition);
        }
        Matcher m = pattern.matcher(input);
        if(m.find()) {
            Token ans = new Token(this, m.group(0), lineNumber, characterPosition);
            return ans;
        } else {
            return null;
        }
    }

    /**
     * Creates a keyword token.
     * This method is for convenience.
     * It is equivalent to
     * {@code new Terminal(name, "^\\Q" + keyword + "\\E", phantom, false)}
     * @param name the name of the keyword.
     * @param keyword the keyword this terminal should match.
     * @param phantom if true, then the terminal will be a phantom.
     * @return a new terminal.
     */
    public static Terminal keyword(String name, String keyword, boolean phantom) {
        Terminal ans = new Terminal(name, "^\\Q" + keyword + "\\E", phantom, false);
        return ans;
    }

    /**
     * Returns true if a terminal of this type should be ignored during parsing.
     * @return true if this terminal is an ignore terminal.
     */
    public boolean ignore() {
        return ignore;
    }

    /**
     * Returns the name of this terminal.
     * @return the name of this terminal.
     */
    public String name() { return name; }

    /**
     * Returns the pattern for this terminal.
     * @return the pattern for this terminal.
     */
    public String pattern() { return pattern.pattern(); }
}

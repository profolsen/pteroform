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

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class TestGrammar {

    public Grammar testGrammar1() {
        Grammar g = new Grammar();
        Rule r1 = new Rule("S", false);
        Rule r2 = new Rule("T", false);
        Rule r3 = new Rule("X", false);
        Terminal t1 = new Terminal("x", "x", false, false);
        Terminal t2 = new Terminal("a", "a", false, false);
        Terminal clearspace = new Terminal("clearspace", "\\s+", false, true);

        r1.addDerivation(r3, r2);
        r2.addDerivation(t1, r3, r2);
        r2.addDerivation(Terminal.epsilon);
        r3.addDerivation(t2);

        g.addRules(r1, r2, r3);
        g.addTerminals(t1, t2, clearspace);

        return g;
    }

    @Test
    public void testStart() {
        assertEquals(new Rule("S", false), testGrammar1().start());
    }

    @Test
    public void testTerminals() {
        HashSet<Terminal> expected = new HashSet<Terminal>();
        expected.add(new Terminal("x", "", false, false));
        expected.add(new Terminal("a", "", false, false));
        expected.add(new Terminal("clearspace", "", false, false));
        expected.add(Terminal.epsilon);
        assertEquals(expected, testGrammar1().terminals());
    }

    @Test
    public void testIgnore() {
        HashSet<Terminal> expected = new HashSet<Terminal>();
        expected.add(new Terminal("clearspace", "", false, false));
        assertEquals(expected, testGrammar1().ignore());
    }

    @Test
    public void testPhantomRules() {
        HashSet<Rule> expected = new HashSet<Rule>();
        assertEquals(expected, testGrammar1().phantomRules());
    }

    @Test
    public void testPhantomTerminals() {
        HashSet<Terminal> expected = new HashSet<Terminal>();
        expected.add(Terminal.epsilon);
        assertEquals(expected, testGrammar1().phantomTerminals());
    }

    @Test
    public void testFollow() {
        //System.out.println(testGrammar1().follow());

        HashSet<Terminal> h1 = new HashSet<Terminal>();
        h1.add(Terminal.EOF);
        HashSet<Terminal> h2 = new HashSet<Terminal>();
        h2.add(Terminal.EOF);
        h2.add(new Terminal("x", "", false, false));
        HashMap<Rule, HashSet<Terminal>> expected = new HashMap<Rule, HashSet<Terminal>>();
        expected.put(new Rule("S", false), h1);
        expected.put(new Rule("T", false), h1);
        expected.put(new Rule("X", false), h2);
        assertEquals(expected, testGrammar1().follow());
    }

    @Test
    public void testParseTable() {
        HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> expected = new HashMap<Terminal, HashMap<String, ArrayList<Symbol>>>();
        Rule s = new Rule("S", false);
        Rule x = new Rule("X", false);
        Rule t = new Rule("T", false);
        Terminal xt = Terminal.keyword("x", "", false);
        Terminal at = Terminal.keyword("a", "", false);
        Terminal clearspace = Terminal.keyword("clearspace", "", false);
        expected.put(Terminal.epsilon, new HashMap<String, ArrayList<Symbol>>());
        expected.put(at, new HashMap<String, ArrayList<Symbol>>());
        expected.put(clearspace, new HashMap<String, ArrayList<Symbol>>());
        expected.put(xt, new HashMap<String, ArrayList<Symbol>>());
        expected.put(Terminal.EOF, new HashMap<String, ArrayList<Symbol>>());
        expected.get(at).put("S", asList(x, t));
        expected.get(at).put("X", asList(at));
        expected.get(xt).put("T", asList(xt, x, t));
        expected.get(Terminal.EOF).put("T", asList(Terminal.epsilon));

        assertEquals(expected, testGrammar1().parseTable());
    }

    public static ArrayList<Symbol> asList(Symbol... symbols) {
        ArrayList<Symbol> ans = new ArrayList<Symbol>();
        for(Symbol s : symbols) ans.add(s);
        return ans;
    }
}

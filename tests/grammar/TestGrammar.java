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
        System.out.println(testGrammar1().follow());
    }

    @Test
    public void testParseTable() {
        System.out.println(testGrammar1().parseTable());
    }
}

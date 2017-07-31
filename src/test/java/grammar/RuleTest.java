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

package test.java.grammar;

import main.java.grammar.Rule;
import main.java.grammar.Terminal;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class RuleTest {


    private static Rule testRule1() {
        Rule r1 = new Rule("R1", false);
        Terminal t = new Terminal("abc+", "[abc]+", false, false);
        Terminal t2 = new Terminal("zz+", "zz+", false, false);
        r1.addDerivation(t, r1, t2);
        r1.addDerivation(Terminal.epsilon);
        return r1;
    }

    private static Rule testRule3() {
        return new Rule("R1", false);
    }

    public static Rule testRule2() {
        Rule r2 = new Rule("R2", true);
        Terminal t = new Terminal("a", "a", false, false);
        Terminal comma = new Terminal("comma", ",", true, false);
        r2.addDerivation(t, comma, r2);
        return r2;
    }

    @Test
    public void testEquals() {
        assertEquals(testRule3(), testRule1());
    }

    @Test
    public void testFirst() {
        Terminal t = new Terminal("abc+", "xx", false, false);
        HashSet<Terminal> expected = new HashSet<Terminal>();
        expected.add(t);
        expected.add(Terminal.epsilon);
        assertEquals(expected, testRule1().first());
    }

    @Test
    public void testToString() {
        assertEquals("R1", testRule1().toString());
    }

    @Test
    public void testPhantom() {
        assertFalse(testRule1().phantom());
        assertTrue(testRule2().phantom());
    }
}

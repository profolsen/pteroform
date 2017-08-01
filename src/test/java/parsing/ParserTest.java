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

package parsing;

import grammar.Grammar;
import grammar.GrammarTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by po917265 on 7/31/17.
 */
public class ParserTest {



    @Test
    public void testParse() {
        Parser p = new Parser(GrammarTest.testGrammar1());

        String expected = "Expected [x, EOF] after a:a, line 1:0";
        SyntaxError actual = null;
        try {
            p.parse("a a");
        } catch(SyntaxError e) {
            actual = e;
        }

        assertEquals(expected, actual.getMessage());
        expected = "Expected [a] after x:x, line 3:8";
        try {
            p.parse("a x \na\n        x x x");
        } catch(SyntaxError e) {
            actual = e;
        }
        assertEquals(expected, actual.getMessage());


        Term t = p.parse("ax  \n a x  a\t   x a");

        assertEquals(2, t.numberOfChildren());
        assertEquals("X", t.getChild(0).value());

        assertEquals(1, t.getChild(0).numberOfChildren());
        assertEquals("a", t.getChild(0).getChild(0).value());
        assertEquals(0, t.getChild(0).getChild(0).numberOfChildren());


        assertEquals("T", t.getChild(1).value());
        assertEquals(3, t.getChild(1).numberOfChildren());
        assertEquals(0, t.getChild(1).getChild(0).numberOfChildren());
        assertEquals("x", t.getChild(1).getChild(0).value());

        assertEquals(1, t.getChild(1).getChild(1).numberOfChildren());
        assertEquals("X", t.getChild(1).getChild(1).value());
        assertEquals("a", t.getChild(1).getChild(1).getChild(0).value());
        assertEquals(0, t.getChild(1).getChild(1).getChild(0).numberOfChildren());

        assertEquals(3, t.getChild(1).getChild(2).numberOfChildren());
    }
}

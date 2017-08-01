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
import grammar.Rule;
import grammar.Terminal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by po917265 on 7/31/17.
 */
public class TermTest {


    public static Parser parser1() {
        Grammar g = new Grammar();
        Rule list = new Rule("list", false);
        Rule element = new Rule("element", false);
        Rule endlist = new Rule("endlist", true);

        Terminal a = Terminal.keyword("a", "a", false);
        Terminal b = Terminal.keyword("b", "b", true);
        Terminal comma = Terminal.keyword("comma", ",", true);
        Terminal clearspace = new Terminal("clearspace", "\\s+", false, true);

        list.addDerivation(element, endlist);
        endlist.addDerivation(Terminal.epsilon);
        endlist.addDerivation(comma, element, endlist);
        element.addDerivation(a);
        element.addDerivation(b);

        g.addTerminals(a, b, comma, clearspace);
        g.addRules(list, element, endlist);

        Parser p = new Parser(g);
        return p;
    }

    @Test
    public void testTerm() {
        Term t = parser1().parse("a, b, a, a, b");
        assertEquals(5, t.numberOfChildren());
        assertEquals(0, t.getChild(1).numberOfChildren());
        assertEquals(0, t.getChild(4).numberOfChildren());
        assertEquals(1, t.getChild(0).numberOfChildren());
        assertEquals(1, t.getChild(2).numberOfChildren());
        assertEquals(1, t.getChild(3).numberOfChildren());
        assertEquals("a", t.getChild(0).getChild(0).value());
        assertEquals("a", t.getChild(2).getChild(0).value());
        assertEquals("a", t.getChild(3).getChild(0).value());
    }
}

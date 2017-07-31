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

import grammar.Terminal;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;
/**
 * Created by po917265 on 7/31/17.
 */
public class TokenizerTest {

    public static HashSet<Terminal> ignore() {
        HashSet<Terminal> answer = new HashSet<Terminal>();

        answer.add(new Terminal("clearspace", "\\s+", false, true));

        return answer;
    }

    public static HashSet<Terminal> terms() {

        HashSet<Terminal> answer = new HashSet<Terminal>();

        answer.add(new Terminal("abc+", "[abc]+", false, false));
        answer.add(Terminal.keyword("comma", ",", false));
        answer.add(Terminal.keyword("xyz", "xyz", false));

        return answer;
    }

    @Test
    public void testNext() {
        Terminal Z = Terminal.keyword("Z", "Z", false);
        Tokenizer t = new Tokenizer("Z aabcc bbbc\ncca xyz xyz \n   cbaabc\n\n", ignore());
        Token x = t.next(Z, false);
        Token y = t.next(Z, true);
        assertEquals(x.value(), y.value());

        Token n = t.next(terms(), true);
        assertEquals("aabcc", n.value());
        assertEquals(1, n.getLineNumber());
        assertEquals(2, n.getCharacterPosition());

        t.next(terms(), true);

        n = t.next(terms(), true);
        assertEquals("cca", n.value());
        assertEquals(2, n.getLineNumber());
        assertEquals(0, n.getCharacterPosition());

        t.next(terms(), true);
        t.next(terms(), true);

        n = t.next(terms(), true);
        assertEquals("cbaabc", n.value());
        assertEquals(3, n.getLineNumber());
        assertEquals(3, n.getCharacterPosition());
    }
}

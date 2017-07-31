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

package test.java.parsing;

import main.java.parsing.Token;
import main.java.grammar.Terminal;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by po917265 on 7/31/17.
 */
public class TokenTest {
    @Test
    public void testToString() {
        Token t1 = testTerminal().parse("abccbazzde", 1, 0);
        String expected = "abccba:abc+";
        String actual = t1.toString();
        assertEquals(expected, actual);
    }

    public static Token testToken() {
        Terminal t = testTerminal();
        Token t1 = t.parse("abccbazzde", 1, 0);
        return t1;
    }

    public static Terminal testTerminal() {
        Terminal t = new Terminal("abc+", "[abc]+", false, false);
        return t;
    }

    @Test
    public void testType() {
        Token t1 = testToken();
        assertEquals(testTerminal().name(), t1.type().name());
    }

    @Test
    public void testValue() {
        Token t1 = testToken();
        assertEquals("abccba", t1.value());
    }

    @Test
    public void testGetChild() {
        Token t1 = testToken();
        Object result = null;
        boolean thrown = false;
        try {
            t1.getChild(0);
        } catch(IndexOutOfBoundsException ioobe) {
            result = ioobe;
        }
        assertNotNull(result);
    }

    @Test
    public void testGetType() {
        Token t1 = testToken();
        Object result = null;
        boolean thrown = false;
        try {
            t1.getType(0);
        } catch(IndexOutOfBoundsException ioobe) {
            result = ioobe;
        }
        assertNotNull(result);
    }

    @Test
    public void testNumberOfChildren() {
        assertEquals(0, testToken().numberOfChildren());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(1, testToken().getLineNumber());
    }

    @Test
    public void testGetCharacterPosition() {
        assertEquals(0, testToken().getCharacterPosition());
    }
}

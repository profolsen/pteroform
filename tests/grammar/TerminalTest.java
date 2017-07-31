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

/**
 * Created by po917265 on 7/27/17.
 */
public class TerminalTest {

    @Test
    public void testFirst() {
        Terminal t = new Terminal("abc", "[abc]", false, false);
        HashSet<Terminal> correct = new HashSet<Terminal>();
        correct.add(t);
        assertEquals(correct, t.first());   //the first set of any terminal is itself.
    }

    @Test
    public void testParse() {
        Terminal t = new Terminal("abc", "[abc]+", false, false);
        String correct = "aabbcc";
        String actual = t.parse("aabbcczzdd", 1, 0).value();
        assertEquals(correct, actual);
        correct = null;
        actual = t.parse("zzaabbcc",1, 0) == null ? null : t.parse("zzaabbcc",1, 0).value();
        assertEquals(correct, actual);
        correct = "";
        actual = Terminal.epsilon.parse("anything it doesn't matter",1, 0).value();
        assertEquals(correct, actual);
        correct = "";
        actual = Terminal.EOF.parse("",1, 0).value();
        assertEquals(correct, actual);
        correct = null;
        t = Terminal.EOF;
        actual = t.parse("zzaabbcc",1, 0) == null ? null : t.parse("zzaabbcc",1, 0).value();
        assertEquals(correct, actual);
    }

}

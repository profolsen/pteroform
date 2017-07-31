package grammar;

import org.junit.Test;
import org.junit.runner.RunWith;

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
        String actual = t.parse("aabbcczzdd").value();
        assertEquals(correct, actual);
        correct = null;
        actual = t.parse("zzaabbcc") == null ? null : t.parse("zzaabbcc").value();
        assertEquals(correct, actual);
        correct = "";
        actual = Terminal.epsilon.parse("anything it doesn't matter").value();
        assertEquals(correct, actual);
        correct = "";
        actual = Terminal.EOF.parse("").value();
        assertEquals(correct, actual);
        correct = null;
        t = Terminal.EOF;
        actual = t.parse("zzaabbcc") == null ? null : t.parse("zzaabbcc").value();
        assertEquals(correct, actual);
    }

}

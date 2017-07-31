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
import grammar.Grammar;
import grammar.Rule;
import grammar.Symbol;
import grammar.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A Parser is an object that uses a test.java.grammar to generate a term from an input string.
 * @author Paul Olsen
 */

public class Parser {

    private Grammar g;
    private HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> parseTable;

    /**
     * Creates a new parser.
     * The test.java.grammar in question needs to be suitable for LL(1) test.java.parsing.
     * @param g a test.java.grammar.
     */
    public Parser(Grammar g) {
        this.g = g;
        parseTable = g.parseTable();
    }

    /**
     * Parses the input string.
     *
     * @throws SyntaxError when the string cannot be parsed.
     * @param s a string.
     * @return a term for the parsed string.
     */
    public Term parse(String s) {
        Term ans = parse(g.start(), new Tokenizer(s, g.ignore()));
        for(Rule r : g.phantomRules()) {
            ans.promote(r.head());
        }
        for(Terminal t : g.phantomTerminals()) {
            ans.deleteAll(t);
        }
        return ans;
    }

    private Term parse(Rule r, Tokenizer source) {
        HashSet<Terminal> possibleParses = new HashSet<Terminal>();
        for(Terminal t : g.terminals()) {
            if(parseTable.get(t).get(r.head()) != null) {
                possibleParses.add(t);
            }
        }
        if(parseTable.get(Terminal.EOF).get(r.head()) != null) {
            possibleParses.add(Terminal.EOF);
        }
        Token next = source.next(possibleParses, false);  //just look ahead.
        if(next == null) {
            throw new SyntaxError("Expected " + possibleParses + " For " + source.source());
        } else {
            ArrayList<Symbol> expansion = parseTable.get(next.type()).get(r.head());
            Term.TermFactory tf = new Term.TermFactory(r.head(), expansion);
            while(tf.next() != null) {
                Symbol s = tf.next();
                if(s instanceof Terminal) {
                    Token t = source.next((Terminal)s, true);
                    if(t == null) {
                        throw new SyntaxError("Expected " + s + " For " + source.source());
                    }
                    tf.setNext(t);  //use it...
                } else {
                    tf.setNext(parse((Rule)s, source));
                }
            }
            return tf.generate();
        }
    }
}

package parsing;

import grammar.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by po917265 on 6/29/17.
 */
public class Parser {

    private Grammar g;
    private HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> parseTable;

    public Parser(Grammar g) {
        this.g = g;
        parseTable = g.parseTable();
    }

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

    public Term parse(Rule r, Tokenizer source) {
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
            System.out.println("pteroform: Syntax Error: Expected " + possibleParses + " For " + source.source());
            return null;
        } else {
            ArrayList<Symbol> expansion = parseTable.get(next.type()).get(r.head());
            Term.TermFactory tf = new Term.TermFactory(r.head(), expansion);
            while(tf.next() != null) {
                Symbol s = tf.next();
                if(s instanceof Terminal) {
                    Token t = source.next((Terminal)s, true);
                    if(t == null) {
                        System.out.println("pteroform: Syntax Error: Expected " + s + " For " + source.source());
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

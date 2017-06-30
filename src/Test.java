import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {

        /*
        Test from: http://faculty.ycp.edu/~dhovemey/fall2010/cs340/lecture/lecture9.html
         */
        /*
        It seems there might??? be an error the parse table in the above.  The following was
        helpful from that point:
        http://digital.cs.usu.edu/~allanv/cs4700/FirstFollow.pdf
        Probably going to use the stack based parsing algorithm described here:
        http://www3.cs.stonybrook.edu/~cse304/Fall08/Lectures/parser-handout.pdf
         */
        Terminal a = Terminal.keyword("a", false);
        Terminal b = Terminal.keyword("b", false);
        Terminal times = Terminal.keyword("*", true);
        Terminal plus = Terminal.keyword("+", true);

        Rule F = new Rule("F", true);
        F.addExpansion(a);
        F.addExpansion(b);

        Rule T1 = new Rule("T1", true);
        T1.addExpansion(Terminal.epsilon);
        T1.addExpansion(times, F, T1);

        Rule T = new Rule("T", false);
        T.addExpansion(F, T1);

        Rule E1 = new Rule("E1", true);
        E1.addExpansion(Terminal.epsilon);

        E1.addExpansion(plus, T, E1);

        Rule E = new Rule("E", false);
        E.addExpansion(T, E1);

        System.out.println(E + " (first--->) " + E.first());
        System.out.println(E1 + " (first--->) " + E1.first());
        System.out.println(T + " (first--->) " + T.first());
        System.out.println(T1 + " (first--->) " + T1.first());
        System.out.println(F + " (first--->) " + F.first());

        Grammar g = new Grammar();
        g.addTerminals(a, b, times, plus, new Terminal("whitespace", "^\\s+", false, true));
        g.addRules(E, E1, T, T1, F);

        HashMap<Rule, HashSet<Terminal>> followMap = g.follow();
        System.out.println(E + " (follow--->) " + followMap.get(E));
        System.out.println(E1 + " (follow--->) " + followMap.get(E1));
        System.out.println(T + " (follow--->) " + followMap.get(T));
        System.out.println(T1 + " (follow--->) " + followMap.get(T1));
        System.out.println(F + " (follow--->) " + followMap.get(F));

        HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> parseTable = g.parseTable();

        System.out.println(a + " (could parse into: )" + parseTable.get(a));
        System.out.println(b + " (could parse into: )" + parseTable.get(b));
        System.out.println(plus + " (could parse into: )" + parseTable.get(plus));
        System.out.println(times + " (could parse into: )" + parseTable.get(times));
        System.out.println(Terminal.EOF + " (could parse into: )" + parseTable.get(Terminal.EOF));

        Parser p = new Parser(g);
        System.out.println(g);
        Term t = p.parse("b*  \ta+a *   b+b+b*a+a+a");
        System.out.println(t);
        /*
        Terminal whitespace = new Terminal("whitespace", "^\\s+", );
        HashSet<Terminal> ignore = new HashSet<Terminal>();
        ignore.add(whitespace);
        System.out.println(p.parse(E, new Tokenizer("a+a", ignore)));
        Term t = p.parse(E, new Tokenizer("b*  \ta+a *   b+b+b*a+a+a", ignore));
        System.out.println(t);
        t.promote("F");
        t.promote("E1");
        t.promote("T1");
        System.out.println(t);
        t.deleteAll(times);
        t.deleteAll(plus);
        t.deleteAll(Terminal.epsilon);
        System.out.println(t);*/
    }
}

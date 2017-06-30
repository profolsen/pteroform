import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {

        /*
        Test from: http://faculty.ycp.edu/~dhovemey/fall2010/cs340/lecture/lecture9.html
         */
        Terminal a = Terminal.keyword("a");
        Terminal b = Terminal.keyword("b");
        Terminal times = Terminal.keyword("*");
        Terminal plus = Terminal.keyword("+");

        Rule F = new Rule("F");
        F.addExpansion(a);
        F.addExpansion(b);

        Rule T1 = new Rule("T1");
        T1.addExpansion(Terminal.epsilon);
        T1.addExpansion(times, F, T1);

        Rule T = new Rule("T");
        T.addExpansion(F, T1);

        Rule E1 = new Rule("E1");
        E1.addExpansion(Terminal.epsilon);

        E1.addExpansion(plus, T, E1);

        Rule E = new Rule("E");
        E.addExpansion(T, E1);

        System.out.println(E + " (first--->) " + E.first());
        System.out.println(E1 + " (first--->) " + E1.first());
        System.out.println(T + " (first--->) " + T.first());
        System.out.println(T1 + " (first--->) " + T1.first());
        System.out.println(F + " (first--->) " + F.first());

        Grammar g = new Grammar();
        g.addTerminals(a, b, times, plus);
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

    }
}

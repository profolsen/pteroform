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
package test;

import grammar.Grammar;
import grammar.Rule;
import grammar.Symbol;
import grammar.Terminal;
import parsing.Parser;
import parsing.Term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) {

        /*
        test.Test from: http://faculty.ycp.edu/~dhovemey/fall2010/cs340/lecture/lecture9.html
         */
        /*
        It seems there might??? be an error the parse table in the above.  The following was
        helpful from that point:
        http://digital.cs.usu.edu/~allanv/cs4700/FirstFollow.pdf
        Probably going to use the stack based parsing algorithm described here:
        http://www3.cs.stonybrook.edu/~cse304/Fall08/Lectures/parser-handout.pdf
         */
        Terminal a = Terminal.keyword("a","a", false);
        Terminal b = Terminal.keyword("b", "b", false);
        Terminal times = Terminal.keyword("times", "*", true);
        Terminal plus = Terminal.keyword("plus", "+", true);

        Rule F = new Rule("F", true);
        F.addDerivation(a);
        F.addDerivation(b);

        Rule T1 = new Rule("T1", true);
        T1.addDerivation(Terminal.epsilon);
        T1.addDerivation(times, F, T1);

        Rule T = new Rule("T", false);
        T.addDerivation(F, T1);

        Rule E1 = new Rule("E1", true);
        E1.addDerivation(Terminal.epsilon);

        E1.addDerivation(plus, T, E1);

        Rule E = new Rule("E", false);
        E.addDerivation(T, E1);

        System.out.println(E + " (first--->) " + E.first());
        System.out.println(E1 + " (first--->) " + E1.first());
        System.out.println(T + " (first--->) " + T.first());
        System.out.println(T1 + " (first--->) " + T1.first());
        System.out.println(F + " (first--->) " + F.first());

        Grammar g = new Grammar();
        g.addTerminals(a, b, times, plus, new Terminal("whitespace", "\\s+", false, true));
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

        System.out.println(Bootstrap.grammar.parseTable());
        System.out.println(Bootstrap.grammar.toString());
        Parser gp = new Parser(Bootstrap.grammar);
        String longerTest =
                "terminals {\n" +
                        "\tkeyword semicolon [phantom] /;/;\n" +
                        "\tkeyword arrow [phantom] /-->/;\n" +
                        "\tkeyword lbracket [phantom] /[/;\n" +
                        "\tkeyword rbracket [phantom] /]/;\n" +
                        "\tkeyword lbrace [phantom] /{/;\n" +
                        "\tkeyword rbrace [phantom] /}/;\n" +
                        " \tkeyword phantom /phantom/;\n" +
                        "\tkeyword ignore /ignore/;\n" +
                        "\tpattern clearspace [ignore] /\\s+/;\n" +
                        "\tpattern identifier /[a-zA-Z][a-zA-Z0-9]*/;\n" +
                        "\tkeyword rulesToken [phantom] /rules/;\n" +
                        "\tkeyword keyword /keyword/;\n" +
                        "\tkeyword pattern /pattern/;\n" +
                        "\tkeyword terminalsToken [phantom] /terminals/;\n" +
                        "\tpattern patternToken /(?<!//)//(?!//).*(?<!//)//(?!//)/;\n" +
                        "} rules {\n" +
                        "\tendOfOptions [phantom] --> epsilon;\n" +
                        "\tendOfOptions --> phantom endOfOptions;\n" +
                        "\tendOfOptions --> ignore endOfOptions;\n" +
                        "\toptions --> epsilon;\n" +
                        "\toptions --> lbracket endOfOptions rbracket;\n" +
                        "\tterminal --> epsilon;\n" +
                        "\tterminal --> keyword identifier options patternToken\n" +
                        "\t\t\tsemicolon terminal;\n" +
                        "\tterminal --> pattern identifier options patternToken\n" +
                        "\t\t\tsemicolon terminal;\n" +
                        "\tterminals [phantom] --> terminalsToken lbrace terminal rbrace;\n" +
                        "\tendOfRule [phantom] --> identifier endOfRule;\n" +
                        "\tendOfRule --> epsilon;\n" +
                        "\trule --> identifier options arrow endOfRule semicolon rule;\n" +
                        "\trule --> epsilon;\n" +
                        "\trules [phantom] --> rulesToken lbrace rule rbrace;\n" +
                        "\ts --> terminals rules;\n" +
                        "\n" +
                        "}";

        longerTest = "terminals {\n" +
                "\tpattern identifier  /^[a-zA-Z][a-zA-Z0-9]*/;\n" +
                "\tpattern rbracker [phantom ] /^^\\Q]\\E/;\n" +
                "\tpattern lbrace [phantom ] /^^\\Q{\\E/;\n" +
                "\tpattern rulesToken [phantom ] /^^\\Qrules\\E/;\n" +
                "\tpattern rbrace [phantom ] /^^\\Q}\\E/;\n" +
                "\tpattern arrow [phantom ] /^^\\Q-->\\E/;\n" +
                "\tpattern patternToken  /^(?<!/)/(?!/).*(?<!/)/(?!/)/;\n" +
                "\tpattern pattern  /^^\\Qpattern\\E/;\n" +
                "\n" +
                "\tpattern phantom  /^^\\Qphantom\\E/;\n" +
                "\tpattern clearspace [ignore ] /^\\s+/;\n" +
                "\tpattern lbracket [phantom ] /^^\\Q[\\E/;\n" +
                "\tpattern terminalsToken [phantom ] /^^\\Qterminals\\E/;\n" +
                "\tpattern ignore  /^^\\Qignore\\E/;\n" +
                "\tpattern keyword  /^^\\Qkeyword\\E/;\n" +
                "\tpattern semicolon [phantom ] /^^\\Q;\\E/;\n" +
                "}\n" +
                "rules {\n" +
                "\tgrammar --> terminals rules;\n" +
                "\toptions --> epsilon;\n" +
                "\toptions --> lbracket endOfOptions rbracker;\n" +
                "\trule --> identifier options arrow endOfRule semicolon rule;\n" +
                "\trule --> epsilon;\n" +
                "\trules[phantom] --> rulesToken lbrace rule rbrace;\n" +
                "\tterminal --> epsilon;\n" +
                "\tterminal --> keyword identifier options patternToken semicolon terminal;\n" +
                "\tterminal --> pattern identifier options patternToken semicolon terminal;\n" +
                "\tendOfOptions[phantom] --> epsilon;\n" +
                "\tendOfOptions --> phantom endOfOptions;\n" +
                "\tendOfOptions --> ignore endOfOptions;\n" +
                "\tendOfRule[phantom] --> identifier endOfRule;\n" +
                "\tendOfRule --> epsilon;\n" +
                "\tterminals[phantom] --> terminalsToken lbrace terminal rbrace;\n" +
                "}";
        Term grammar = gp.parse(longerTest);
        System.out.println(grammar);
        test2();
        /*
        grammar.Terminal whitespace = new grammar.Terminal("whitespace", "^\\s+", );
        HashSet<grammar.Terminal> ignore = new HashSet<grammar.Terminal>();
        ignore.add(whitespace);
        System.out.println(p.parse(E, new parsing.Tokenizer("a+a", ignore)));
        parsing.Term t = p.parse(E, new parsing.Tokenizer("b*  \ta+a *   b+b+b*a+a+a", ignore));
        System.out.println(t);
        t.promote("F");
        t.promote("E1");
        t.promote("T1");
        System.out.println(t);
        t.deleteAll(times);
        t.deleteAll(plus);
        t.deleteAll(grammar.Terminal.epsilon);
        System.out.println(t);*/
    }

    private static void test2() {
        Grammar g = new Grammar();
        Terminal a = Terminal.keyword("a", "a", false);
        Terminal b = Terminal.keyword("b", "b", false);
        Terminal clearspace = new Terminal("clearspace", "\\s+", false, true);

        Rule r1 = new Rule("S", false);
        r1.addDerivation(a, r1, b);
        r1.addDerivation(Terminal.epsilon);
        g.addTerminals(a, b, clearspace);
        g.addRules(r1);
        Parser p = new Parser(g);
        System.out.println(p.parse("a  a a a  a\tbbbb\nb"));
    }
}

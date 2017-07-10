import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by po917265 on 7/1/17.
 */
public class Bootstrap {
    public static Grammar grammar = new Grammar();
    static {
        Terminal semicolon = Terminal.keyword("semicolon", ";", true);
        Terminal arrow = Terminal.keyword("arrow", "-->", true);
        Terminal lbracket = Terminal.keyword("lbracket", "[", true);
        Terminal phantom = Terminal.keyword("phantom", "phantom", false);
        Terminal ignore = Terminal.keyword("ignore", "ignore", false);
        Terminal rbracket = Terminal.keyword("rbracker", "]", true);
        Terminal clearspace = new Terminal("clearspace", "\\s+", false, true);
        Terminal identifier = new Terminal("identifier", "[a-zA-Z][a-zA-Z0-9]*", false, false);
        Terminal epsilon = Terminal.epsilon;
        Terminal lbrace = Terminal.keyword("lbrace", "{", true);
        Terminal rbrace = Terminal.keyword("rbrace", "}", true);
        Terminal rulesToken = Terminal.keyword("rulesToken", "rules", true);

        Terminal patternToken = new Terminal("patternToken", "(?<!/)/(?!/).*(?<!/)/(?!/)", false, false);
        Terminal keyword = Terminal.keyword("keyword", "keyword", false);
        Terminal pattern = Terminal.keyword("pattern", "pattern", false);
        Terminal terminalsToken = Terminal.keyword("terminalsToken", "terminals", true);



        Rule endOfOptions = new Rule("endOfOptions", true);
        endOfOptions.addExpansion(epsilon);
        endOfOptions.addExpansion(phantom, endOfOptions);
        endOfOptions.addExpansion(ignore, endOfOptions);
        Rule options = new Rule("options", false);
        options.addExpansion(epsilon);
        options.addExpansion(lbracket, endOfOptions, rbracket);
        Rule terminal = new Rule("terminal", false);
        terminal.addExpansion(epsilon);
        terminal.addExpansion(keyword, identifier, options, patternToken, semicolon, terminal);
        terminal.addExpansion(pattern, identifier, options, patternToken, semicolon, terminal);
        Rule terminals = new Rule("terminals", true);
        terminals.addExpansion(terminalsToken, lbrace, terminal, rbrace);
        Rule endOfRule = new Rule("endOfRule", true);
        endOfRule.addExpansion(identifier, endOfRule);
        endOfRule.addExpansion(epsilon);
        Rule rule = new Rule("rule", false);
        rule.addExpansion(identifier, options, arrow, endOfRule, semicolon, rule);
        rule.addExpansion(epsilon);
        Rule rules = new Rule("rules", true);
        rules.addExpansion(rulesToken, lbrace, rule, rbrace);
        Rule s = new Rule("grammar", false);
        s.addExpansion(terminals, rules);

        grammar.addRules(s, rules, rule, endOfRule, endOfOptions, options, terminal, terminals);
        grammar.addTerminals(semicolon, arrow, clearspace, identifier, lbracket, rbracket, phantom, ignore, lbrace, rbrace, rulesToken, patternToken, keyword, pattern, terminalsToken);
        bootstrap();
    }

    private static void bootstrap() {
        Parser p = new Parser(grammar);
        String input = "";
        try {
            Scanner scan = new Scanner(new File("bootstrap/bootstrap.ptero"));
            while(scan.hasNextLine()) {
                input += scan.nextLine() + "\n";
            }
            scan.close();
            Term t = p.parse(input);
            convertToGrammar(t);
        } catch(IOException ioe) {
            System.out.println("pteroform: fatal error: Can't open bootstrap/bootstrap.ptero.  Exiting.");
            System.exit(1);
        }
    }

    private static void convertToGrammar(Term t) {
        grammar = new Grammar();
        HashMap<String, Terminal> terminals = new HashMap<String, Terminal>();
        terminals.put("epsilon", Terminal.epsilon);
        grammar.addTerminals(Terminal.epsilon);
        generateTerminal(t.getTerm(0), terminals);
        HashMap<String, Rule> rules = new HashMap<String, Rule>();
        generateNonTerminals(t.getTerm(1), rules);
        generateExpansions(t.getTerm(1), rules, terminals);
    }

    private static void generateTerminal(Term term, HashMap<String, Terminal> terminals) {
        if(term.size() < 1) return;  //
        Token type = term.getToken(0);
        Token name = term.getToken(1);
        Term options = term.getTerm(2);
        Token pattern = term.getToken(3);
        boolean phantom = false;
        boolean ignore = false;
        if(options.size() != 0) {
            for(int i = 0; i < options.size(); i++) {
                if(options.getToken(i).value().equals("phantom")) {
                    phantom = true;
                } else if(options.getToken(i).value().equals("ignore")) {
                    ignore = true;
                }
            }
        }
        Terminal toAdd;
        if(type.value().equals("keyword")) {
            toAdd = Terminal.keyword(name.value(), pattern.value().substring(1, pattern.value().length() - 1), phantom);
        } else {
            toAdd = new Terminal(name.value(), pattern.value().substring(1, pattern.value().length() - 1), phantom, ignore);
        }
        grammar.addTerminals(toAdd);
        terminals.put(toAdd.name(), toAdd);
        generateTerminal(term.getTerm(4), terminals);
    }

    private static void generateNonTerminals(Term term, HashMap<String, Rule> rules) {
        Term t = term;
        while(t.size() > 0) {
            boolean phantom = options(t);
            rules.put(t.getToken(0).value(), new Rule(t.getToken(0).value(), phantom));
            t = t.getTerm(t.size() - 1);
        }
    }

    private static void generateExpansions(Term term, HashMap<String, Rule> rules, HashMap<String, Terminal> terminals) {
        Term t = term;
        while(t.size() > 0) {
            Symbol[] expansion = new Symbol[t.size() - 3]; //-1 for id, -1 for options, -1 for recursion.
            for(int i = 2; i < t.size() - 1; i++) {
                Symbol correct = terminals.containsKey(t.getToken(i).value()) ? terminals.get(t.getToken(i).value()) :
                            rules.get(t.getToken(i).value());
                expansion[i - 2] = correct;
            }
            rules.get(t.getToken(0).value()).addExpansion(expansion);
            t = t.getTerm(t.size() - 1);
        }
        for(Rule r : rules.values()) {
            grammar.addRules(r);
        }
    }

    private static boolean options(Term t) {
        if(t.getTerm(1).size() > 0) {
            if(t.getTerm(1).getToken(0).value().equals("phantom")) {
                return true;
            }
        }
        return false;
    }


}

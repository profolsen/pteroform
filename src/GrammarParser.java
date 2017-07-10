import java.io.File;
import java.util.Scanner;

/**
 * Created by po917265 on 7/1/17.
 */
public class GrammarParser {
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
        Scanner scan = new Scanner(new File("bootstrap/bootstrap.ptero"));
    }


}

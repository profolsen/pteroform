/**
 * Created by po917265 on 7/1/17.
 */
public class GrammarParser {
    public static final Grammar grammar = new Grammar();
    static {
        Terminal semicolon = Terminal.keyword(";", true);
        Terminal arrow = Terminal.keyword("-->", true);
        Terminal optionToken = Terminal.keyword("options", true);
        Terminal lbracket = Terminal.keyword("[", true);
        Terminal phantom = Terminal.keyword("phantom", false);
        Terminal ignore = Terminal.keyword("ignore", false);
        Terminal rbracket = Terminal.keyword("]", true);
        Terminal clearspace = new Terminal("clearspace", "\\s+", false, true);
        Terminal identifier = new Terminal("identifier", "[a-zA-Z][a-zA-Z0-9]*", false, false);
        Terminal epsilon = Terminal.epsilon;
        Terminal lbrace = Terminal.keyword("{", true);
        Terminal rbrace = Terminal.keyword("}", true);
        Terminal rulesToken = Terminal.keyword("rules", true);
        Rule endOfOptions = new Rule("endOfOptions", true);
        endOfOptions.addExpansion(epsilon);
        endOfOptions.addExpansion(phantom, endOfOptions);
        endOfOptions.addExpansion(ignore, endOfOptions);
        Rule options = new Rule("options", false);
        options.addExpansion(epsilon);
        options.addExpansion(optionToken, lbracket, endOfOptions, rbracket);
        Rule endOfRule = new Rule("endOfRule", true);
        endOfRule.addExpansion(identifier, endOfRule);
        endOfRule.addExpansion(epsilon);
        Rule rule = new Rule("rule", false);
        rule.addExpansion(identifier, options, arrow, endOfRule, semicolon, rule);
        rule.addExpansion(epsilon);
        Rule rules = new Rule("rules", false);
        rules.addExpansion(rulesToken, lbrace, rule, rbrace);
        grammar.addRules(rules, rule, endOfRule, endOfOptions, options);
        grammar.addTerminals(semicolon, arrow, clearspace, identifier, optionToken, lbracket, rbracket, phantom, ignore, lbrace, rbrace, rulesToken);
    }
}

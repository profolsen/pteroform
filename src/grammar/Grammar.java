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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A Grammar is a set of rules that describe how to parse a string into a term.
 * @author Paul Olsen
 *
 */
public class Grammar {

    private HashSet<Rule> rules;
    private HashSet<Terminal> terminals;
    private Rule start;

    /**
     * Creates a Grammar.
     */
    public Grammar() {
        rules = new HashSet<Rule>();
        terminals = new HashSet<Terminal>();
        terminals.add(Terminal.epsilon);  //epsilon is always here.
        start = null;
    }

    /**
     * Adds the given rules to this grammar.
     * The first rule added to the grammar will become the start rule.
     * @param list a list of rules.
     */
    public void addRules(Rule... list) {
        for(Rule r : list) {
            rules.add(r);
        }
        if(start == null) {
            start = list[0];
        }
    }

    /**
     * Adds the list of terminals to the grammar.
     * @param list a list of terminals.
     */
    public void addTerminals(Terminal... list) {
        for(Terminal t : list) {
            terminals.add(t);
        }
    }

    /**
     * Computes the follow sets for each rule in the grammar.
     * @return the follow sets for every rule in the grammar.
     */
    public HashMap<Rule, HashSet<Terminal>> follow() {
        HashMap<Rule, HashSet<Terminal>> followMap = new HashMap<Rule, HashSet<Terminal>>();
        for(Rule r : rules) {
            followMap.put(r, new HashSet<Terminal>());
        }
        followMap.get(start).add(Terminal.EOF);
        update_horizontal(followMap);   // I believe this only has to be done once
                                        // because the update only depends on predict/first sets
                                        // which don't depend on follow (there's no dependency on follow).
        while(vertical_update(followMap))
            ;   //do nothing.
        return followMap;
    }

    /**
     * Generates a parse table for the grammar.
     * @return the generated parse table.
     */
    public HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> parseTable() {
        HashMap<Terminal, HashMap<String, ArrayList<Symbol>>> answer = new HashMap<Terminal, HashMap<String, ArrayList<Symbol>>>();
        for(Terminal t : terminals) {
            if(t.equals(Terminal.epsilon)) {
                answer.put(Terminal.EOF, new HashMap<String, ArrayList<Symbol>>());
            }
            answer.put(t, new HashMap<String, ArrayList<Symbol>>());
        }
        HashMap<Rule, HashSet<Terminal>> followMap = follow();
        for(Rule r : rules) {
            for(ArrayList<Symbol> expansion : r) {
                HashSet<Terminal> first = first(expansion);
                for(Terminal t : first) {
                    if (t.equals(Terminal.epsilon)) continue;  //don't think we should do this for epsilon.
                    if (first.contains(t)) {
                        if (!answer.get(t).containsKey(r.head())) {
                            answer.get(t).put(r.head(), expansion);
                        } else {
                            System.out.println("Ambiguous derivation for " + t + " and " + r.head() + " --> " + expansion);
                        }
                    }
                }
                if(first.contains(Terminal.epsilon)) {
                    for(Terminal t : followMap.get(r)) {
                        if (!answer.get(t).containsKey(r.head())) {
                            answer.get(t).put(r.head(), expansion);
                        } else {
                            System.out.println("Ambiguous derivation for " + t + " and " + r.head() + " --> " + expansion);
                        }
                    }
                }
            }
        }

        return answer;
    }

    /**
     * Returns the start rule.
     * @return the start rule.
     */
    public Rule start() {
        return start;
    }

    private boolean vertical_update(HashMap<Rule, HashSet<Terminal>> followMap) {
        boolean answer = false;
        for(Rule r : followMap.keySet()) {
            for(ArrayList<Symbol> expansion : r) {
                for(int i = expansion.size() - 1; i >= 0; i--) {
                    Symbol s = expansion.get(i);
                    if(s instanceof Rule && first(expansion.subList(i+1, expansion.size())).contains(Terminal.epsilon)) {
                        answer = followMap.get((Rule)s).addAll(followMap.get(r)) || answer;
                    }
                }
            }
        }
        return answer;
    }

    private boolean update_horizontal(HashMap<Rule, HashSet<Terminal>> followMap) {
        boolean answer = false;
        HashSet<Terminal> runningFirst = new HashSet<Terminal>();
        for(Rule r : followMap.keySet()) {
            for(ArrayList<Symbol> expansion : r) {
                for(int i = 0; i < expansion.size()-1; i++) {
                    Symbol s = expansion.get(i);
                    if(s instanceof Rule) {
                        HashSet<Terminal> toAdd = first(expansion.subList(i+1, expansion.size()));
                        toAdd.remove(Terminal.epsilon);
                        answer = followMap.get((Rule)s).addAll(toAdd) || answer;
                    }
                }
            }
        }
        return answer;
    }

    private HashSet<Terminal> first(List<Symbol> symbols) {
        HashSet<Terminal> answer = new HashSet<Terminal>();
        boolean containsEpsilon = true;
        for(int i = 0; i < symbols.size(); i++) {
            Symbol s = symbols.get(i);
            HashSet<Terminal> first = s.first();
            answer.addAll(first);
            answer.remove(Terminal.epsilon);
            if(! first.contains(Terminal.epsilon)) {
                containsEpsilon = false;
                break;
            }
        }
        if(containsEpsilon) answer.add(Terminal.epsilon);
        return answer;
    }

    /**
     * Returns a set of all terminals.
     * @return all the terminals in the grammar.
     */
    public HashSet<Terminal> terminals() {
        return terminals;
    }

    /**
     * Returns a human readable string representation of the grammar.
     * @return a string.
     */
    public String toString() {
        String ans = "terminals {\n";
        for(Terminal t : terminals) {
            ans += asString(t) + "\n";
        }
        ans += "}\nrules {\n";
        ans += asString(start) + "\n";
        for(Rule r : rules) {
            if(!r.equals(start)) ans += asString(r) + "\n";
        }
        ans += "}";
        return ans;
    }

    private String asString(Terminal t) {
        if(t.equals(Terminal.EOF) || t.equals(Terminal.epsilon)) {
            return "";
        }
        String ans = "\tpattern " + t.name() + " " + optionsAsString(t) + " /" + t.pattern() + "/;";
        return ans;
    }

    private String optionsAsString(Terminal t) {
        String ans = "[";
        if(t.phantom()) {
            ans += "phantom ";
        }
        if(t.ignore()) {
            ans += "ignore ";
        }
        ans += "]";
        if(ans.length() > 2) return ans;
        return "";
    }

    private String asString(Rule r) {
        String ans = "";
        boolean first = true;
        for(ArrayList<Symbol> expansion : r) {
            ans += "\t" + r.head() + (first ? optionsAsString(r) : "") +  " --> ";
            first = false;
            for(int i = 0; i < expansion.size() - 1; i++) {
                ans += (expansion.get(i) instanceof Rule ? expansion.get(i) : ((Terminal)expansion.get(i)).name()) + " ";
            }
            if(expansion.size() > 0) {
                ans += expansion.get(expansion.size() - 1) + ";\n";
            }
        }
        return ans.substring(0, ans.length() - 1); //get rid of final \n.
    }

    private String optionsAsString(Rule r) {
        if(phantomRules().contains(r)) {
            return "[phantom]";
        }
        return "";
    }

    /**
     * Computes a set of all terminals that should be ignored.
     * @return the computed set.
     */
    public HashSet<Terminal> ignore() {
        HashSet<Terminal> ans = new HashSet<Terminal>();
        for(Terminal t : terminals) {
            if(t.ignore()) {
                ans.add(t);
            }
        }
        return ans;
    }

    /**
     * computes a set of all phantom rules.
     * @return the computed set.
     */
    public HashSet<Rule> phantomRules() {
        HashSet<Rule> phantomRules = new HashSet<Rule>();
        for(Rule r : rules) {
            if(r.phantom()) phantomRules.add(r);
        }
        return phantomRules;
    }

    /**
     * Computes a set of all phantom terminals.
     * @return the computed set.
     */
    public HashSet<Terminal> phantomTerminals() {
        HashSet<Terminal> phantomTerminals = new HashSet<Terminal>();
        for(Terminal t : terminals) {
            if(t.phantom()) phantomTerminals.add(t);
        }
        return phantomTerminals;
    }
}

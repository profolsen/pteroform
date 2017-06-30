import com.sun.xml.internal.xsom.impl.Ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by po917265 on 6/29/17.
 */
public class Grammar {

    private HashSet<Rule> rules;
    private HashSet<Terminal> terminals;
    private Rule start;

    public Grammar() {
        rules = new HashSet<Rule>();
        terminals = new HashSet<Terminal>();
        terminals.add(Terminal.epsilon);  //epsilon is always here.
        start = null;
    }

    public void addRules(Rule... list) {
        for(Rule r : list) {
            rules.add(r);
        }
        if(start == null) {
            start = list[0];
        }
    }

    public void addTerminals(Terminal... list) {
        for(Terminal t : list) {
            terminals.add(t);
        }
    }

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
}

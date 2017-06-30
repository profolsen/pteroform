import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by po917265 on 6/29/17.
 */
public class Rule implements Symbol, Iterable<ArrayList<Symbol>> {

    private ArrayList<ArrayList<Symbol>> expansions;
    private String head;

    public Rule(String head) {
        this.head = head;
        expansions = new ArrayList<ArrayList<Symbol>>();
    }

    public int hashCode() {
        return head.hashCode();
    }

    public boolean equals(Object other) {
        if(!(other instanceof Rule)) {
            return false;
        } else {
            return ((Rule) other).head.equals(head);
        }
    }

    public HashSet<Terminal> first() {
        HashSet<Terminal> answer = new HashSet<Terminal>();
        for(ArrayList<Symbol> expansion : expansions) {
            answer.addAll(first(expansion));
        }
        //System.out.println(head + " returning: " + answer);
        return answer;
    }

    private HashSet<Terminal> first(ArrayList<Symbol> expansion) {
        HashSet<Terminal> answer = new HashSet<Terminal>();
        boolean canExpandToEpsilon = true;
        for(Symbol s : expansion) {
            HashSet<Terminal> childFirst = s.first();
            answer.addAll(childFirst);
            answer.remove(Terminal.epsilon);
            //System.out.println(s + " returned " + childFirst);
            if(!childFirst.contains(Terminal.epsilon)) {
                canExpandToEpsilon = false;
                break;
            }
        }
        if(canExpandToEpsilon) {
            answer.add(Terminal.epsilon);
        }
        return answer;
    }

    public ArrayList<Symbol> follow() {
        return null;
    }

    public void addExpansion(Symbol symbol) {
        ArrayList<Symbol> expansion = new ArrayList<Symbol>();
        expansion.add(symbol);
        expansions.add(expansion);
    }

    public void addExpansion(Symbol... derivation) {
        ArrayList<Symbol> expansion = new ArrayList<Symbol>();
        for(int i = 0; i < derivation.length; i++) {
            expansion.add(derivation[i]);
        }
        expansions.add(expansion);
    }

    public String toString() {
        return head;
    }

    @Override
    public Iterator<ArrayList<Symbol>> iterator() {
        return expansions.iterator();
    }

    public String head() {
        return head;
    }
}

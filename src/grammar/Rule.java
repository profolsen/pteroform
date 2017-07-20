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
import java.util.HashSet;
import java.util.Iterator;

/**
 * A rule roughly corresponds to a non-terminal symbol when talking about grammars.
 * Each rule can have several derivations (represented as an ArrayList of Symbols).
 * A rule can also potentially be a <b>phantom</b>, which means that it is used to parse and recognize the string
 * but the non-terminal will not appear in the final parsed Term.
 */
public class Rule implements Symbol, Iterable<ArrayList<Symbol>> {

    private ArrayList<ArrayList<Symbol>> expansions;
    private String head;
    private boolean phantom;

    /**
     * Creates a new rule with a given name (non-terminal symbol) and possibly makes it a phantom term.
     * @param head the non-terminal associated with the rule.
     * @param phantom if true, this rule will be a phantom.
     */
    public Rule(String head, boolean phantom) {
        this.head = head;
        expansions = new ArrayList<ArrayList<Symbol>>();
        this.phantom = phantom;
    }

    /**
     * Returns a hashcode for this rule.
     * The hashcode is the head's hashcode.
     * @return head.hashCode();
     */
    public int hashCode() {
        return head.hashCode();
    }

    /**
     * Tests if this Rule equals another objects.
     * It returns true only when other is a rule and its head is equal to this rule's head.
     * @param other another object.
     * @return true if other is a rule and has the same head as this rule.
     */
    public boolean equals(Object other) {
        if(!(other instanceof Rule)) {
            return false;
        } else {
            return ((Rule) other).head.equals(head);
        }
    }

    /**
     * Returns the first set for this rule.  This is to facillitate LL(1) parsing.
     * @return the first set for this rule.
     */
    public HashSet<Terminal> first() {
        HashSet<Terminal> answer = new HashSet<Terminal>();
        for(ArrayList<Symbol> expansion : expansions) {
            answer.addAll(first(expansion));
        }
        //System.out.println(head + " returning: " + answer);
        return answer;
    }

    //helper method for the above method.
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

    /**
     * Adds an derivation to this rule.
     * When parsing the non-terminal associated with this rule can be replaced with the derivation supplied here, or
     *              with another derivation.
     * @param derivation the derivation to add.
     */
    public void addDerivation(Symbol... derivation) {
        ArrayList<Symbol> expansion = new ArrayList<Symbol>();
        for(int i = 0; i < derivation.length; i++) {
            expansion.add(derivation[i]);
        }
        expansions.add(expansion);
    }

    /**
     * Returns the head.
     * This method should be used when a message about the head is desired.
     * @return the head.
     */
    public String toString() {
        return head;
    }

    @Override
    public Iterator<ArrayList<Symbol>> iterator() {
        return expansions.iterator();
    }

    /**
     * Returns the head.
     * This method should be used when a person wants to know what the head of a rule is.
     * @return the head.
     */
    public String head() {
        return head;
    }

    /**
     * Returns true if this rule is a phantom.
     * @return true if this rule is a phantom.
     */
    public boolean phantom() {
        return phantom;
    }
}

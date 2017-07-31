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
package parsing;

import grammar.Rule;
import grammar.Symbol;
import grammar.Terminal;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by po917265 on 6/30/17.
 */
public class Term implements TermNode{
    private HashMap<Integer, Term> subTerms;
    private HashMap<Integer, Token> tokens;
    private String head;
    private ArrayList<Symbol> derivation;

    /**
     * Creates a new term.
     * The maps used are from child index to symbol (either term or token).
     * Given the term S(a, S(a, b), b), where the first and third children are tokens and the second symbol is a term,
     * the maps would be:
     * subTerm: [1=S(a,b)]
     * and
     * tokens: [0=a,2=b].
     *
     * @param head the non-terminal symbol at the root of the term.
     * @param derivation a list of symbols explaining what each child should be.
     * @param subTerms a map of subterms.
     * @param tokens a map of tokens.
     */
    private Term(String head, ArrayList<Symbol> derivation, HashMap<Integer, Term> subTerms, HashMap<Integer, Token> tokens) {
        this.subTerms = subTerms;
        this.tokens = tokens;
        this.head = head;
        this.derivation = derivation;
    }

    /**
     * Returns the term at the given index.
     * If there is a token not a term at the given index then this method returns null.
     * @param index an index.
     * @return the term at the given index.
     */
    public Term getTerm(int index) {
        return subTerms.get(index);
    }

    /**
     * Returns the token at the given index.
     * If there is a term not a token at the given index then this method returns null.
     * @param index an index.
     * @return the token at the given index.
     */
    public Token getToken(int index) {
        return tokens.get(index);
    }

    @Override
    public String value() {
        return head;
    }

    @Override
    public TermNode getChild(int index) {
        if(getType(index) instanceof Terminal) return getToken(index);
        return getTerm(index);
    }

    /**
     * Returns the type of the child at the specified index.
     * The type will either be a Rule (meaning that the child at that index will be a term).
     * Or the type will be a Terminal (meaning that the child at that index will be a token).
     * @param index an index
     * @return the type for the correct index.
     */
    public Symbol getType(int index) {
        return derivation.get(index);
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }

    /**
     * Returns the root non-terminal symbol of this term.
     * @return the root symbol.
     */
    public String head() {
        return value();
    }

    /**
     * Eliminates the specified non-terminal from the term.
     * Promotion is an operation where a certain non-terminal symbol is eliminated from the term without removing any tokens from the term.
     * Given the following term,
     * S(a, X(c, S(a, b), d), b)
     * The result of promoting the non-terminal symbol X would be:
     * S(a, c, S(a, b), d, b).
     * This process is done recursively, so that every instance of the specified non-terminal symbol will be eliminated, regardless of where it appears in the term.
     * @param head what non-terminal symbol to promote.
     */
    public void promote(String head) {
        if(subTerms.size() != 0) {
            for(Term t : subTerms.values()) {
                t.promote(head);
            }
            HashMap<Integer, Term> newSubTerms = new HashMap<Integer, Term>();
            HashMap<Integer, Token> newTokens = new HashMap<Integer, Token>();
            ArrayList<Symbol> newExpansion = new ArrayList<Symbol>();
            int j = 0;
            for(int i = 0; i < derivation.size(); i++) {
                if(derivation.get(i) instanceof Terminal) {
                    newTokens.put(j++, tokens.get(i));
                    newExpansion.add(derivation.get(i));
                } else if(((Rule) derivation.get(i)).head().equals(head)) {
                    Term t = subTerms.get(i);
                    for(int k = 0; k < t.derivation.size(); k++) {
                        newExpansion.add(t.derivation.get(k));
                        if(t.derivation.get(k) instanceof Terminal) {
                            newTokens.put(j++, t.tokens.get(k));
                        } else {
                            newSubTerms.put(j++, t.subTerms.get(k));
                        }
                    }
                } else {
                    newSubTerms.put(j++, subTerms.get(i));
                    newExpansion.add(derivation.get(i));
                }
            }
            subTerms = newSubTerms;
            tokens = newTokens;
            derivation = newExpansion;
        }
    }

    /**
     * Removes all tokens of the specified type (Terminal) from the term.
     * @param t a token type to remove.
     */
    public void deleteAll(Terminal t) {
        HashMap<Integer, Term> newSubTerms = new HashMap<Integer, Term>();
        HashMap<Integer, Token> newTokens = new HashMap<Integer, Token>();
        ArrayList<Symbol> newExpansion = new ArrayList<Symbol>();
        int j = 0;
        for(int i = 0; i < derivation.size(); i++) {
            if(derivation.get(i) instanceof Rule) {
                subTerms.get(i).deleteAll(t);
                newSubTerms.put(j++, subTerms.get(i));
                newExpansion.add(derivation.get(i));
            } else if(! tokens.get(i).type().equals(t)) {
                newTokens.put(j++, tokens.get(i));
                newExpansion.add(derivation.get(i));
            }
        }
        tokens = newTokens;
        subTerms = newSubTerms;
        derivation = newExpansion;
    }

    /**
     * Returns a human readable string representation of the term.
     * @return a human readable string representation of the term.
     */
    @Override
    public String toString() {
        String ans = head + "(";
        for(int i = 0; i < derivation.size() - 1; i++) {
            ans += (getTerm(i) == null ? getToken(i) : getTerm(i)) + ", ";
        }
        int max = derivation.size() - 1;
        if(max > -1) {
            ans += getTerm(max) == null ? getToken(max) : getTerm(max);
        }
        ans += ")";
        return ans;
    }

    /**
     * Returns the number of children of this term.
     * @return
     */
    public int size() {
        return derivation.size();
    }

    //TODO: add documentation.
    public static class TermFactory {
        private HashMap<Integer, Term> subTerms;
        private HashMap<Integer, Token> tokens;
        private ArrayList<Symbol> expansion;
        private String head;
        private int i;

        public TermFactory(String head, ArrayList<Symbol> expansion) {
            subTerms = new HashMap<Integer, Term>();
            tokens = new HashMap<Integer, Token>();
            this.head = head;
            this.expansion = expansion;
            i = 0;
        }

        public Symbol next() {
            if(i < expansion.size()) return expansion.get(i);
            return null;
        }

        public Symbol setNext(Token token) {
            if(next() instanceof Terminal) {
                Terminal t = (Terminal)next();
                if(t.equals(token.type())) {
                    tokens.put(i++, token);
                    return null;
                }
            }
            Symbol ans = next();
            i++;
            return ans;
        }

        public Symbol setNext(Term term) {
            if(next() instanceof Rule) {
                Rule r = (Rule)next();
                if(r.head().equals(term.head)) {
                    subTerms.put(i++, term);
                    return null;
                }
            }
            Symbol ans = next();
            i++;
            return ans;
        }

        public Term generate() {
            return new Term(head, expansion, subTerms, tokens);
        }


    }
}

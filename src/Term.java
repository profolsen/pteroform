import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by po917265 on 6/30/17.
 */
public class Term {
    private HashMap<Integer, Term> subTerms;
    private HashMap<Integer, Token> tokens;
    private String head;
    private ArrayList<Symbol> expansion;

    private Term(String head, ArrayList<Symbol> expansion, HashMap<Integer, Term> subTerms, HashMap<Integer, Token> tokens) {
        this.subTerms = subTerms;
        this.tokens = tokens;
        this.head = head;
        this.expansion = expansion;
    }

    public Term getTerm(int index) {
        return subTerms.get(index);
    }

    public Token getToken(int index) {
        return tokens.get(index);
    }

    public Symbol getType(int index) {
        return expansion.get(index);
    }

    public String head() {
        return head;
    }

    public void promote(String head) {
        if(subTerms.size() != 0) {
            for(Term t : subTerms.values()) {
                t.promote(head);
            }
            HashMap<Integer, Term> newSubTerms = new HashMap<Integer, Term>();
            HashMap<Integer, Token> newTokens = new HashMap<Integer, Token>();
            ArrayList<Symbol> newExpansion = new ArrayList<Symbol>();
            int j = 0;
            for(int i = 0; i < expansion.size(); i++) {
                if(expansion.get(i) instanceof Terminal) {
                    newTokens.put(j++, tokens.get(i));
                    newExpansion.add(expansion.get(i));
                } else if(((Rule)expansion.get(i)).head().equals(head)) {
                    Term t = subTerms.get(i);
                    for(int k = 0; k < t.expansion.size(); k++) {
                        newExpansion.add(t.expansion.get(k));
                        if(t.expansion.get(k) instanceof Terminal) {
                            newTokens.put(j++, t.tokens.get(k));
                        } else {
                            newSubTerms.put(j++, t.subTerms.get(k));
                        }
                    }
                } else {
                    newSubTerms.put(j++, subTerms.get(i));
                    newExpansion.add(expansion.get(i));
                }
            }
            subTerms = newSubTerms;
            tokens = newTokens;
            expansion = newExpansion;
        }
    }

    public void deleteAll(Terminal t) {
        HashMap<Integer, Term> newSubTerms = new HashMap<Integer, Term>();
        HashMap<Integer, Token> newTokens = new HashMap<Integer, Token>();
        ArrayList<Symbol> newExpansion = new ArrayList<Symbol>();
        int j = 0;
        for(int i = 0; i < expansion.size(); i++) {
            if(expansion.get(i) instanceof Rule) {
                subTerms.get(i).deleteAll(t);
                newSubTerms.put(j++, subTerms.get(i));
                newExpansion.add(expansion.get(i));
            } else if(! tokens.get(i).type().equals(t)) {
                newTokens.put(j++, tokens.get(i));
                newExpansion.add(expansion.get(i));
            }
        }
        tokens = newTokens;
        subTerms = newSubTerms;
        expansion = newExpansion;
    }

    public String toString() {
        String ans = "(" + head + ",";
        for(int i = 0; i < expansion.size() - 1;i++) {
            ans += (getTerm(i) == null ? getToken(i) : getTerm(i)) + ",";
        }
        int max = expansion.size() - 1;
        if(max > -1) {
            ans += getTerm(max) == null ? getToken(max) : getTerm(max);
        }
        ans += ")";
        return ans;
    }

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

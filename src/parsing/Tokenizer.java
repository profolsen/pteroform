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

import grammar.Terminal;
import grammar.Token;

import java.util.HashSet;

/**
 * The Tokenizer class converts a string into tokens one at a time for the parser.
 * This class is probably redundant.
 * Java has a Tokenizer that is probably far more sophisticated.
 */
public class Tokenizer {

    private String source;
    private HashSet<Terminal> ignore;

    /**
     * Creates a new tokenizer which reads the source string.
     * Any token created by the tokens in the ignore set are ignored.
     * These tokens can only be used to separate tokens returned by this Tokenizer.
     * @param source the source string.
     * @param ignore a set of token types to ignore.
     */
    public Tokenizer(String source, HashSet<Terminal> ignore) {
        this.source = source;
        this.ignore = ignore;
        ignore();
    }

    /**
     * Returns the next token in the string.
     * If there is more than one possible next token, the longest one is returned.
     * This method may return null if no terminal from possibleParses matches the source string.
     * @param possibleParses a set of possible token types which could come next.
     * @param use indicates whether the read token should be removed from the beginning of the source string.
     *           If use is true, the token will be removed.
     * @return the next Token in the string.
     */
    public Token next(HashSet<Terminal> possibleParses, boolean use) {
        Token best = null;
        for(Terminal t : possibleParses) {
            Token hypothetical = t.parse(source);
            if(hypothetical != null && (best == null || hypothetical.value().length() > best.value().length())) {
                best = hypothetical;
            }
        }
        if(use && best != null) {
            source = source.substring(best.value().length()); //skip to the next begin spot.
            ignore();
        }
        return best;
    }

    /**
     * Returns the token in the source string.
     * This method will return null if t does not match the source string.
     * @param t a token type to look for.
     * @param use if use is true, the tokenizer will advance over the token in the source string.
     * @return a token or null if t does not match the string.
     */
    public Token next(Terminal t, boolean use) {
        HashSet<Terminal> arg = new HashSet<Terminal>();
        arg.add(t);
        return next(arg, use);
    }


    private void ignore() {
        Token best = null;
        for(Terminal t : ignore) {
            Token hypothetical = t.parse(source);
            if(hypothetical != null && (best == null || hypothetical.value().length() > best.value().length())) {
                best = hypothetical;
            }
        }
        if(best != null) {
            source = source.substring(best.value().length()); //skip to the next begin spot.
        }
    }

    /**
     * Returns the current source string.
     * @return the current source string.
     */
    public String source() {
        return source;
    }
}

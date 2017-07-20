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
 * Created by po917265 on 6/30/17.
 */
public class Tokenizer {

    private String source;
    private HashSet<Terminal> ignore;

    public Tokenizer(String source, HashSet<Terminal> ignore) {
        this.source = source;
        this.ignore = ignore;
        ignore();
    }

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

    public Token next(Terminal t, boolean use) {
        HashSet<Terminal> arg = new HashSet<Terminal>();
        arg.add(t);
        return next(arg, use);
    }

    public void ignore() {
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

    public String source() {
        return source;
    }
}

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

import grammar.Symbol;
import grammar.Terminal;

/**
 * Created by po917265 on 6/30/17.
 */
public class Token implements TermNode{
    private Terminal type;
    private String value;

    /**
     * Creates a new Token.
     * @param type the type of this token.
     *             I.e., what terminal generated this token.
     * @param value the string value of this token.
     */
    public Token(Terminal type, String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return value + ":" + type;
    }

    /**
     * Returns the type of this token.
     * @return the type of this token.
     */
    public Terminal type() {
        return type;
    }

    /**
     * Returns the value of this token.
     * @return the value of this token.
     */
    public String value() {
        return value;
    }

    @Override
    public TermNode getChild(int index) {
        throw new IndexOutOfBoundsException("Index " + index + ", Size: 0");
    }

    @Override
    public Symbol getType(int index) {
        throw new IndexOutOfBoundsException("Index " + index + ", Size: 0");
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }
}

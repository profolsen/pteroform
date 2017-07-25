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

import grammar.Terminal;

import java.util.HashSet;

/**
 * Symbol is a common interface for both Rules and Terminals.
 */
public interface Symbol {

    /**
     * Returns the first set of the symbol.
     * For terminals, this returns the terminal itself.
     * For rules, the first set, as discussed in <a href="http://faculty.ycp.edu/~dhovemey/fall2010/cs340/lecture/lecture9.html">David Hovemeyer's</a> lecture notes is generated and returned.
     * @return the first set for this terminal.
     */
    public HashSet<Terminal> first();

    /**
     * Returns true if this symbol is a phantom.
     * Phantom symbols are used to recognize structure within a string, but do not appear in the final parsed term for the string.
     * @return true if this symbol is a phantom.
     */
    public boolean phantom();
}

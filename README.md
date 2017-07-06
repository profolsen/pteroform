Pteroform
===

This project provides an implementation of an interpreter interpreter.
Two input files are to be provided to the main program:
1. a grammar-interpreter file.  This file contains the grammar for the language, which the parser uses to produce terms. 
This file also contains additional syntax checks to be performed on the term as well as a description of how to execute the program a term represents.
2. a program file.  This file contains a program to be interpreted.

The program file's format is dependent entirely on the grammar, syntax checks, and interpreter specified in the grammar-interpreter file.
Therefore, the remainder of this document focusses on the format and function of the grammar-interpreter file.

<h3>Grammar Syntax</h3>
A grammar for a language is made up of a list of terminals and a list of non-terminals called rules.

<h4>Terminals.</h4>
During parsing, the input file must be interpreted as tokens. 
This process is tokenization (i.e., breaking up a string into strings which when concatenated in the correct order give the original file).
Terminals are either named regular expressions or named strings that describe how the program file is to be tokenized.
Every token has exactly one terminal that the tokenizer used to produce the token.
An example of a terminal is:
<pre>pattern alpha /a/;</pre>
In this example, a token is created named alpha.
The pattern for the alpha terminal is found at the end of the line, between matching `/` characters.
In this case, the alpha terminal can be used to generate tokens with the string value 'a', since that is the only thing the pattern for the alpha token can recognize.
Every terminal declaration ends with a semicolon.

For convenience, a second kind of terminal is provided called the keyword terminal.
An example of a keyword terminal is:
<pre>keyword dot /./;</pre>
In this example, a terminal is created called dot which can only be used by the tokenizer to generate tokens with the string value '.'.

<b>Terminal Options</b>
Terminals have two options:
1. Phantom Terminals.
Though these terminals will be parsed and can be used in rule productions, they will never appear in a term created by the parser from a program file.
The purpose of such terminals is to allow the existence of a terminal useful for parsing but which would clutter up the term created by the parser.
2. Ignore Terminals.
Ignore terminals are used to create tokens that do not appear in the grammar (e.g., comments and clear space).
The primary purpose for ignore tokens is to separate meaningful tokens from each other.

Options appear as a space delineated list enclosed in matching square brackets ('[', and ']') right after the terminal name.
For example, 
<pre>pattern clearspace [ignore] /\s+/;</pre>
creates a terminal named clearspace which will match and remove all clearspace from the input.
The clearspace can still be used to separate other tokens.

<h4>Rules</h4>
The rules (or productions) of a grammar tell the parser how to structure parsed tokens from the program file.
An example of some rules describing a grammar is:
<pre>rules {
    S --> a S b;
    S --> epsilon;
}
</pre>
In this example, `a`, `b`, are terminals, each capable of reading the next 'a' or 'b' string from input, respectively.
`epsilon` is a special terminal available to use in any production.
It stands in for an empty string.
This grammar describes how to structure the parsed tokens in strings beginning with some number of a's and followed by the same number of b's.
For example, the string `aaaaabbbbb` will be parsed into ten tokens: 5 a's and 5 b's.
This grammar gives the correct way to structure these tokens into a single term.
That term will be:
`S(a, S(a, S(a, S(a, S(a, S(), b), b), b), b), b)`.

There are some restrictions on rules:
1. Every terminal that appears on the right hand side of a rule (to the right of the arrow symbol `-->`) must be defined in the `terminals { ... }` part of the grammar.
An important exception is the `epsilon` terminal, which is automatically defined for every grammar.
2. Any non-terminal symbol that appears on the right hand side of a rule must appear on the left hand side of a rule at least once.

Just like in the example above, a non-terminal can appear on the left hand side of multiple rules (The symbol 'S' is on the left hand side of both rules in this grammar).

<h4>Additional Syntax Checks</h4>




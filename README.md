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
    S --> Y;
    Y --> epsilon;
}
</pre>
In this example, `a`, `b`, are terminals, each capable of reading the next 'a' or 'b' string from input, respectively.
`epsilon` is a special terminal available to use in any production.
It stands in for an empty string.
This grammar describes how to structure the parsed tokens in strings beginning with some number of a's and followed by the same number of b's.
For example, the string `aaaaabbbbb` will be parsed into ten tokens: 5 a's and 5 b's.
This grammar gives the correct way to structure these tokens into a single term.
That term will be:
`S(a, S(a, S(a, S(a, S(a, Y(), b), b), b), b), b)`.

There are some restrictions on rules:
1. Every terminal that appears on the right hand side of a rule (to the right of the arrow symbol `-->`) must be defined in the `terminals { ... }` part of the grammar.
An important exception is the `epsilon` terminal, which is automatically defined for every grammar.
2. Any non-terminal symbol that appears on the right hand side of a rule must appear on the left hand side of a rule at least once.

Just like in the example above, a non-terminal can appear on the left hand side of multiple rules (The symbol 'S' is on the left hand side of both rules in this grammar).

As with terminals (see <b>Token Options</b> above), a rule can be assigned two options: phantom and ignore.
A rule using the phantom option will be parsed and structured, but the non-terminal on the left side of a phantom rule will not appear anywhere in the term parsed using the grammar.
For example, if in the grammar above the rule Y were defined:
<pre>Y [phantom] --> epsilon;</pre>
Then the resulting term for the string`aaaaabbbbb` would be:
`S(a, S(a, S(a, S(a, S(a, b), b), b), b), b)`.  Note that the term Y() is not present.
The phantom option applies to non-terminal symbols, not rules.
If a rule with a left hand side of `Z` is given the phantom option, then `Z` will not appear in a term parsed by the grammar, even if there is another rule with `Z` on the left hand side that does not use the phantom option.

The ignore option is also available for non-terminals, but has no effects because it is not yet implemented.
<h4>Additional Syntax Checks</h4>

Examples:
<pre>rule(left, options(*), *, $X, *) 
     requires terminal($X, *) or rule($X, *) as
     "Undefined terminal or non-terminal symbol $X";</pre>
     
<pre>terminal($name, *) once as
     "Duplicate terminal definition as $name";</pre>

<pre>
identifier($varname) requires 
     declaration($varname) in block(*) as
     "Variable $varname never declared";</pre>
     
1. `*` matches 0 or many arguments.
2. `+`  matches 1 or many arguments.
3. `{x, y}`  matches at least x arguments, at most y arguments.
4. `$VARIABLE` all structure from this point downward is bound to variable.
When `$VARIABLE` appears on the right hand side of `requires` it is replaced with the term `$VARIABLE` is equal to.
5. `once` means that the term left of it should not appear more than once.
6. `or` signifies two alternative terms.  Either being matched means match success.
7. `and` signifies two requirements.  Either failing to match means no match failure.
8. `in` Gives a parent term which both the left and right hand side (of `requires`) must be under.
9. `$VARIABLE:TYPE` the variable must have the specific type.
Either `TYPE` terminal or `TYPE` non-terminal generated the term to which `$VARIABLE` is bound.


<b>Restrictions:</b>
1. No negative search.
Given a subterm, another subterm is searched for.
The absence of a given subterm can never be ruled out.
2. First order.
variables are not allowed to be present except at leaves of terms.

<b>Process:</b>
1. We search the parsed term (pt) for each occurrence of left hand side of the `requires`.
2. Every variable on the left hand side is matched to the corresponding sub-term in the pt.
3. We search the pt for a term or terms which satisfy the condition on the right side of the `requires`.
4. If no satisfying term or terms can be found, print the error message appearing after the `as`, using variable interpolation to provide detailed information.


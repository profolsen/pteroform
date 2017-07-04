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




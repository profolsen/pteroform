The Grammar for grammars
=====
The grammar for grammars described in its own grammar:
<pre>
terminals {
    ignore /\s+/;
    keyword /ignore/;
    keyword /keyword/;
    keyword /hide/;
    keyword /-->/;
    keyword /\//;
    keyword /terminals/;
    keyword /{/;
    keyword /}/;
    keyword /rules/;
}
</pre>
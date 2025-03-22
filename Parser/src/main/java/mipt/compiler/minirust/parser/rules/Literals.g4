grammar Literals;

INTEGER_LITERAL: DIGIT+;

IDENTIFIER
    :   (LETTER | '_') (LETTER | DIGIT | '_')*
    ;

fragment LETTER
    :   [a-zA-Z] ;

fragment DIGIT
    :   [0-9] ;
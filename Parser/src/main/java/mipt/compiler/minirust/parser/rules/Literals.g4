grammar Literals;

INTEGER_LITERAL : DIGIT+ ;
FLOAT_LITERAL   : DIGIT+ '.' DIGIT+ ;
BOOLEAN_LITERAL : 'true' | 'false' ;

IDENTIFIER : (LETTER | '_') (LETTER | DIGIT | '_')* ;

fragment LETTER : [a-zA-Z] ;
fragment DIGIT  : [0-9] ;
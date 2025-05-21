grammar Expressions;

import Types, Literals;

expression
    : logicalExpression
    ;

logicalExpression
    : comparisonExpression (('&&' | '||') comparisonExpression)*
    ;

comparisonExpression
    : arithmeticExpression (COMPARISON_OP arithmeticExpression)?
    ;

arithmeticExpression
    : term (('+' | '-') term)*
    ;

term
    : factor (('*' | '/') factor)*
    ;

factor
    : literalExpression
    | identifierExpression
    | functionCall
    | '(' expression ')'
    ;

literalExpression
    : INTEGER_LITERAL
    | FLOAT_LITERAL
    | BOOLEAN_LITERAL
    ;

identifierExpression
    : IDENTIFIER
    ;

functionCall
    : IDENTIFIER '(' argumentList? ')'
    ;

argumentList
    : expression (',' expression)*
    ;

COMPARISON_OP
    : '==' | '!=' | '>' | '<' | '>=' | '<='
    ;
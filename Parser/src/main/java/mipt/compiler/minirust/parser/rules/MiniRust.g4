grammar MiniRust;

import Expressions;

program : functionDeclaration+ EOF ;

statement
    : letStatement
    | assignment
    | ifStatement
    | whileStatement
    | loopStatement
    | printStatement
    | expressionStatement
    ;

statementIf
    : assignment
    | ifStatement
    | printStatement
    | expressionStatement
    ;

letStatement
    : 'let' ('mut')? identifierExpression TYPE ('=' expression)? ';'
    ;

assignment
    : identifierExpression '=' expression ';'
    ;

ifStatement
    : 'if' '(' expression ')' '{' statementIf* '}'
      ( 'else if' '(' expression ')' '{' statementIf* '}' )*
      ( 'else' '{' statementIf* '}' )?
    ;

whileStatement
    : 'while' '(' expression ')' '{' statement* '}'
    ;

loopStatement
    : 'loop' '{' statement* '}'
    ;

printStatement
    : 'print' '(' expression ')' ';'
    ;

expressionStatement
    : expression ';'
    ;

functionDeclaration
    : 'fn' IDENTIFIER '(' parameterList? ')' block
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : IDENTIFIER TYPE
    ;

block
    : '{' statement* '}'
    ;

WS: [ \t\r\n]+ -> skip ;
COMMENT : '//' ~[\r\n]* -> skip ;
MULTILINE_COMMENT : '/*' .*? '*/' -> skip ;

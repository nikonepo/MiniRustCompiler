grammar MiniRust;

import Expressions;

program         : 'fn' 'main()' '{' statement* '}' EOF;

statement       : letStatement
                | assignment
                | ifStatement
                | printStatement
                ;

statementIf     : assignment
                | ifStatement
                | printStatement
                ;

letStatement :
   'let' identifierExpression TYPE ('=' expression ';') ? ;

assignment :
    identifierExpression '=' expression ';' ;

ifStatement     : 'if' '(' comparisonExpression ')' '{' statementIf* '}';

printStatement  : 'print' '(' expression ')' ';' ;

WS: [ \t\r\n]+ -> skip;
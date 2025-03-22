grammar Expressions;

import Types, Literals;

expression : arithmeticExpression | operatorExpression ;

operatorExpression :
    arithmeticExpression
  | comparisonExpression;

arithmeticExpression
    : term (('+' | '-') term)* ;
term
    : factor (('*' | '/') factor)* ;
factor
    : literalExpression | identifierExpression
    | '(' arithmeticExpression ')';

comparisonExpression :
     arithmeticExpression '==' arithmeticExpression
   | arithmeticExpression '!=' arithmeticExpression
   | arithmeticExpression '>' arithmeticExpression
   | arithmeticExpression '<' arithmeticExpression
   | arithmeticExpression '>=' arithmeticExpression
   | arithmeticExpression '<=' arithmeticExpression ;

literalExpression :
   INTEGER_LITERAL;

identifierExpression :
   IDENTIFIER;

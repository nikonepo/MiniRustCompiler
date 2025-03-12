grammar Expressions;

import Types, Literals;

expression : literalExpression | operatorExpression ;

literalExpression :
   INTEGER_LITERAL;

identifierExpression:
   IDENTIFIER;

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

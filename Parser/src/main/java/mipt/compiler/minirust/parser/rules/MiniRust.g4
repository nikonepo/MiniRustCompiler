grammar MiniRust;

// The starting rule for parsing expressions
expr
    : expr ('+' | '-') term   # AddSub
    | term                    # SingleTerm
    ;

term
    : term ('*' | '/') factor # MulDiv
    | factor                  # SingleFactor
    ;

factor
    : NUMBER                  # Number
    | '(' expr ')'            # Parentheses
    ;

// Lexer rules (tokens). ANTLR automatically recognizes these as token types.
NUMBER: [0-9]+ ('.' [0-9]+)?; // Matches integers and decimals
WS: [ \t\r\n]+ -> skip;       // Skip whitespace
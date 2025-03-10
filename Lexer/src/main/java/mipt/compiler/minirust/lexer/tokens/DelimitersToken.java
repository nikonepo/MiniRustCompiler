package mipt.compiler.minirust.lexer.tokens;

public enum DelimitersToken implements TokenType {
    LEFT_CURLY_BRACKET("{"),
    RIGHT_CURLY_BRACKET("}"),
    LEFT_PARENTHESES("("),
    RIGHT_PARENTHESES(")");

    private final String symbol;

    DelimitersToken(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

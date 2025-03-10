package mipt.compiler.minirust.parser.tokens;

public enum PunctuationsToken implements TokenType {

    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    PERCENT("%"),
    NOT("!"),
    EQ("="),
    EQEQ("=="),
    NE("!="),;

    private final String symbol;

    PunctuationsToken(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}

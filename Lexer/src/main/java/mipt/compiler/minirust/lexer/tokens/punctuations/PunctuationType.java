package mipt.compiler.minirust.lexer.tokens.punctuations;

public enum PunctuationType {

    PLUS("+", PlusPunctuation::new),
    MINUS("-", MinusPunctuation::new),
    STAR("*", StarPunctuation::new),
    SLASH("/", SlashPunctuation::new),
    PERCENT("%", PercentPunctuation::new),
    NOT("!", NotPunctuation::new),
    EQ("=", EqPunctuation::new),
    EQEQ("==", EqEqPunctuation::new),
    NE("!=", NePunctuation::new),
    SEMI(";", SemiPunctuation::new);

    private final PunctuationTokenConstructor constructor;
    private final String                      symbol;

    PunctuationType(String symbol, PunctuationTokenConstructor constructor) {
        this.symbol = symbol;
        this.constructor = constructor;
    }

    public String getSymbol() {
        return symbol;
    }

    public PunctuationToken construct() {
        return constructor.construct();
    }

    @FunctionalInterface
    interface PunctuationTokenConstructor {

        PunctuationToken construct();
    }
}

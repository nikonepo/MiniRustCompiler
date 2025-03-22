package mipt.compiler.minirust.lexer.tokens.keywords;

public enum KeywordType {

    ELSE("else", ElseKeyword::new),
    FALSE("false", FalseKeyword::new),
    FN("fn", FnKeyword::new),
    IF("if", IfKeyword::new),
    LET("let", LetKeyword::new),
    TRUE("true", TrueKeyword::new);

    private final KeywordTokenConstructor constructor;
    private final String                  keyword;

    KeywordType(String keyword, KeywordTokenConstructor constructor) {
        this.keyword = keyword;
        this.constructor = constructor;
    }

    public String getKeyword() {
        return keyword;
    }

    public KeywordToken createToken() {
        return constructor.construct();
    }

    public static KeywordType fromString(String word) {
        // TODO: use a map instead of iterating over the values
        for (KeywordType token : KeywordType.values()) {
            if (token.keyword.equals(word)) {
                return token;
            }
        }

        return null;
    }

    @FunctionalInterface
    interface KeywordTokenConstructor {

        KeywordToken construct();
    }
}

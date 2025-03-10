package mipt.compiler.minirust.lexer.tokens;

public enum KeywordsToken implements TokenType {

    ELSE("else"),
    FALSE("false"),
    FN("fn"),
    IF("if"),
    LET("let"),
    TRUE("true");

    private final String word;

    KeywordsToken(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public static KeywordsToken fromString(String word) {
        // TODO: use a map instead of iterating over the values
        for (KeywordsToken token : KeywordsToken.values()) {
            if (token.word.equals(word)) {
                return token;
            }
        }

        return null;
    }
}

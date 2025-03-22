package mipt.compiler.minirust.lexer.tokens.keywords;

import mipt.compiler.minirust.lexer.tokens.TokenType;

public abstract class KeywordToken implements TokenType {

    private final KeywordType type;

    protected KeywordToken(KeywordType type) {
        this.type = type;
    }

    public final KeywordType getType() {
        return type;
    }
}

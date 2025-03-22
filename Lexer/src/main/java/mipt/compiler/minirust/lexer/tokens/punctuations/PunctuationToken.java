package mipt.compiler.minirust.lexer.tokens.punctuations;

import mipt.compiler.minirust.lexer.tokens.TokenType;

public abstract class PunctuationToken implements TokenType {

    private final PunctuationType type;

    protected PunctuationToken(PunctuationType type) {
        this.type = type;
    }
}

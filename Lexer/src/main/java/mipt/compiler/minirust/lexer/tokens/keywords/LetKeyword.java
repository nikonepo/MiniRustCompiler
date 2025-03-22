package mipt.compiler.minirust.lexer.tokens.keywords;

import mipt.compiler.minirust.lexer.tokens.TokenType;

public final class LetKeyword extends KeywordToken {

    public LetKeyword() {
        super(KeywordType.LET);
    }
}

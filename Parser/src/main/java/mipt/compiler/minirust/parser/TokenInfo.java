package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.tokens.TokenType;

public record TokenInfo(TokenType token, Position position) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof TokenInfo otherTokenInfo)) {
            return false;
        }

        return token.equals(otherTokenInfo.token) && position.equals(otherTokenInfo.position);
    }
}

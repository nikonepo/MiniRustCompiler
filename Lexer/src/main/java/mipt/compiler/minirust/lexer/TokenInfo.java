package mipt.compiler.minirust.lexer;

import mipt.compiler.minirust.lexer.tokens.DelimitersToken;
import mipt.compiler.minirust.lexer.tokens.IdentifierToken;
import mipt.compiler.minirust.lexer.tokens.IntegerLiteral;
import mipt.compiler.minirust.lexer.tokens.KeywordsToken;
import mipt.compiler.minirust.lexer.tokens.PunctuationsToken;
import mipt.compiler.minirust.lexer.tokens.TokenType;

public record TokenInfo(TokenType token, Position start, Position end) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof TokenInfo otherTokenInfo)) {
            return false;
        }

        return token.equals(otherTokenInfo.token) && start.equals(otherTokenInfo.start) &&
            end.equals(otherTokenInfo.end);
    }

    @Override
    public String toString() {
        return String.format("""
            TokenType: %s
            Position:
                Start: line: %d, column: %d
                End: line: %d, column: %d
            """, token.toString(), start.line, start.column, end.line, end.column);
    }

    // === Literal Tokens === //

    public static TokenInfo INTEGER_LITERAL(Position position, int value) {
        return new TokenInfo(
            new IntegerLiteral(value),
            position,
            Position.of(position.column + String.valueOf(value).length(), position.line)
        );
    }

    // === Identifier Tokens === //

    public static TokenInfo IDENTIFIER(Position position, String key) {
        return new TokenInfo(
            new IdentifierToken(key),
            position,
            Position.of(position.column + key.length(), position.line)
        );
    }

    // === Delimiters Tokens === //

    public static TokenInfo LEFT_CURLY_BRACKET(Position position) {
        return new TokenInfo(
            DelimitersToken.LEFT_CURLY_BRACKET,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo RIGHT_CURLY_BRACKET(Position position) {
        return new TokenInfo(
            DelimitersToken.RIGHT_CURLY_BRACKET,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo LEFT_PARENTHESES(Position position) {
        return new TokenInfo(
            DelimitersToken.LEFT_PARENTHESES,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo RIGHT_PARENTHESES(Position position) {
        return new TokenInfo(
            DelimitersToken.RIGHT_PARENTHESES,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    // === Keywords Tokens === //

    public static TokenInfo ELSE(Position position) {
        return new TokenInfo(
            KeywordsToken.ELSE,
            position,
            Position.of(position.column + 4, position.line)
        );
    }

    public static TokenInfo FALSE(Position position) {
        return new TokenInfo(
            KeywordsToken.FALSE,
            position,
            Position.of(position.column + 5, position.line)
        );
    }

    public static TokenInfo FN(Position position) {
        return new TokenInfo(
            KeywordsToken.FN,
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo IF(Position position) {
        return new TokenInfo(
            KeywordsToken.IF,
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo LET(Position position) {
        return new TokenInfo(
            KeywordsToken.LET,
            position,
            Position.of(position.column + 3, position.line)
        );
    }

    public static TokenInfo TRUE(Position position) {
        return new TokenInfo(
            KeywordsToken.TRUE,
            position,
            Position.of(position.column + 4, position.line)
        );
    }

    // === Punctuations Tokens === //

    public static TokenInfo PLUS(Position position) {
        return new TokenInfo(
            PunctuationsToken.PLUS,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo MINUS(Position position) {
        return new TokenInfo(
            PunctuationsToken.MINUS,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo STAR(Position position) {
        return new TokenInfo(
            PunctuationsToken.STAR,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo SLASH(Position position) {
        return new TokenInfo(
            PunctuationsToken.SLASH,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo PERCENT(Position position) {
        return new TokenInfo(
            PunctuationsToken.PERCENT,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo NOT(Position position) {
        return new TokenInfo(
            PunctuationsToken.NOT,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo EQ(Position position) {
        return new TokenInfo(
            PunctuationsToken.EQ,
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo EQEQ(Position position) {
        return new TokenInfo(
            PunctuationsToken.EQEQ,
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo NE(Position position) {
        return new TokenInfo(
            PunctuationsToken.NE,
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo SEMI(Position position) {
        return new TokenInfo(
            PunctuationsToken.SEMI,
            position,
            Position.of(position.column + 1, position.line)
        );
    }
}

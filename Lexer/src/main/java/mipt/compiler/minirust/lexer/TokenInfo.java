package mipt.compiler.minirust.lexer;

import mipt.compiler.minirust.lexer.tokens.IdentifierToken;
import mipt.compiler.minirust.lexer.tokens.IntegerLiteral;
import mipt.compiler.minirust.lexer.tokens.TokenType;
import mipt.compiler.minirust.lexer.tokens.delimiters.LeftCurlyBracketDelimiter;
import mipt.compiler.minirust.lexer.tokens.delimiters.LeftParentheses;
import mipt.compiler.minirust.lexer.tokens.delimiters.RightCurlyBracket;
import mipt.compiler.minirust.lexer.tokens.delimiters.RightParentheses;
import mipt.compiler.minirust.lexer.tokens.keywords.ElseKeyword;
import mipt.compiler.minirust.lexer.tokens.keywords.FalseKeyword;
import mipt.compiler.minirust.lexer.tokens.keywords.FnKeyword;
import mipt.compiler.minirust.lexer.tokens.keywords.IfKeyword;
import mipt.compiler.minirust.lexer.tokens.keywords.LetKeyword;
import mipt.compiler.minirust.lexer.tokens.keywords.TrueKeyword;
import mipt.compiler.minirust.lexer.tokens.punctuations.EqEqPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.EqPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.MinusPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.NePunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.NotPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.PercentPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.PlusPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.SemiPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.SlashPunctuation;
import mipt.compiler.minirust.lexer.tokens.punctuations.StarPunctuation;

public record TokenInfo(TokenType token, Position start, Position end) {

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof TokenInfo otherTokenInfo)) {
            return false;
        }

        return token.getClass().equals(otherTokenInfo.token.getClass()) &&
            start.equals(otherTokenInfo.start) && end.equals(otherTokenInfo.end);
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
        return new TokenInfo(new IntegerLiteral(value),
            position,
            Position.of(position.column + String.valueOf(value).length(), position.line)
        );
    }

    // === Identifier Tokens === //

    public static TokenInfo IDENTIFIER(Position position, String key) {
        return new TokenInfo(new IdentifierToken(key),
            position,
            Position.of(position.column + key.length(), position.line)
        );
    }

    // === Delimiters Tokens === //

    public static TokenInfo LEFT_CURLY_BRACKET(Position position) {
        return new TokenInfo(new LeftCurlyBracketDelimiter(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo RIGHT_CURLY_BRACKET(Position position) {
        return new TokenInfo(new RightCurlyBracket(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo LEFT_PARENTHESES(Position position) {
        return new TokenInfo(new LeftParentheses(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo RIGHT_PARENTHESES(Position position) {
        return new TokenInfo(new RightParentheses(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    // === Keywords Tokens === //

    public static TokenInfo ELSE(Position position) {
        return new TokenInfo(new ElseKeyword(),
            position,
            Position.of(position.column + 4, position.line)
        );
    }

    public static TokenInfo FALSE(Position position) {
        return new TokenInfo(new FalseKeyword(),
            position,
            Position.of(position.column + 5, position.line)
        );
    }

    public static TokenInfo FN(Position position) {
        return new TokenInfo(new FnKeyword(),
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo IF(Position position) {
        return new TokenInfo(new IfKeyword(),
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo LET(Position position) {
        return new TokenInfo(new LetKeyword(),
            position,
            Position.of(position.column + 3, position.line)
        );
    }

    public static TokenInfo TRUE(Position position) {
        return new TokenInfo(new TrueKeyword(),
            position,
            Position.of(position.column + 4, position.line)
        );
    }

    // === Punctuations Tokens === //

    public static TokenInfo PLUS(Position position) {
        return new TokenInfo(new PlusPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo MINUS(Position position) {
        return new TokenInfo(new MinusPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo STAR(Position position) {
        return new TokenInfo(new StarPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo SLASH(Position position) {
        return new TokenInfo(new SlashPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo PERCENT(Position position) {
        return new TokenInfo(new PercentPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo NOT(Position position) {
        return new TokenInfo(new NotPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo EQ(Position position) {
        return new TokenInfo(new EqPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }

    public static TokenInfo EQEQ(Position position) {
        return new TokenInfo(new EqEqPunctuation(),
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo NE(Position position) {
        return new TokenInfo(new NePunctuation(),
            position,
            Position.of(position.column + 2, position.line)
        );
    }

    public static TokenInfo SEMI(Position position) {
        return new TokenInfo(new SemiPunctuation(),
            position,
            Position.of(position.column + 1, position.line)
        );
    }
}

package mipt.compiler.minirust.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mipt.compiler.minirust.lexer.TokenInfo;
import mipt.compiler.minirust.lexer.tokens.DelimitersToken;
import mipt.compiler.minirust.lexer.tokens.IdentifierToken;
import mipt.compiler.minirust.lexer.tokens.IntegerLiteral;
import mipt.compiler.minirust.lexer.tokens.KeywordsToken;
import mipt.compiler.minirust.lexer.tokens.PunctuationsToken;

public class Tokenizer {

    public static List<TokenInfo> tokenize(String input) {
        var tokens = new ArrayList<TokenInfo>();

        var currentPosition = new ParserPosition();

        while (currentPosition.position < input.length()) {
            var c = input.charAt(currentPosition.position);

            if (isWhitespace(c)) {
                if (c == '\n') {
                    ++currentPosition.line;
                    currentPosition.column = 0;
                } else {
                    ++currentPosition.column;
                }

                ++currentPosition.position;

                continue;
            } else if (Character.isDigit(c)) {
                tokens.add(parseNumber(input, currentPosition));

                continue;
            } else if (Character.isAlphabetic(c)) {
                tokens.add(parseIdentifier(input, currentPosition));

                continue;
            }

            switch (c) {
                // Delimiters

                case '{':
                    tokens.add(new TokenInfo(DelimitersToken.LEFT_CURLY_BRACKET,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '}':
                    tokens.add(new TokenInfo(DelimitersToken.RIGHT_CURLY_BRACKET,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '(':
                    tokens.add(new TokenInfo(DelimitersToken.LEFT_PARENTHESES,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case ')':
                    tokens.add(new TokenInfo(DelimitersToken.RIGHT_PARENTHESES,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                // Punctuations

                case '+':
                    tokens.add(new TokenInfo(PunctuationsToken.PLUS,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '-':
                    tokens.add(new TokenInfo(PunctuationsToken.MINUS,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '*':
                    tokens.add(new TokenInfo(PunctuationsToken.STAR,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '/':
                    tokens.add(new TokenInfo(PunctuationsToken.SLASH,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '%':
                    tokens.add(new TokenInfo(PunctuationsToken.PERCENT,
                        new Position(currentPosition)
                    ));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '=':
                    if (currentPosition.position + 1 < input.length() &&
                        input.charAt(currentPosition.position + 1) == '=') {
                        tokens.add(new TokenInfo(PunctuationsToken.EQEQ,
                            new Position(currentPosition)
                        ));

                        currentPosition.column += 2;
                        currentPosition.position += 2;
                    } else {
                        tokens.add(new TokenInfo(
                            PunctuationsToken.EQ,
                            new Position(currentPosition)
                        ));

                        ++currentPosition.column;
                        ++currentPosition.position;
                    }

                    break;

                case '!':
                    if (currentPosition.position + 1 < input.length() &&
                        input.charAt(currentPosition.position + 1) == '=') {
                        tokens.add(new TokenInfo(PunctuationsToken.NE,
                            new Position(currentPosition)
                        ));

                        currentPosition.column += 2;
                        currentPosition.position += 2;
                    } else {
                        tokens.add(new TokenInfo(PunctuationsToken.NOT,
                            new Position(currentPosition)
                        ));

                        ++currentPosition.column;
                        ++currentPosition.position;
                    }

                    break;
            }
        }

        return tokens;
    }

    /**
     * The following characters are considered whitespace:
     * <li> U+0009 (horizontal tab, '\t')
     * <li> U+000A (line feed, '\n')
     * <li> U+000B (vertical tab)
     * <li> U+000C (form feed)
     * <li> U+000D (carriage return, '\r')
     * <li> U+0020 (space, ' ')
     * <li> U+0085 (next line)
     * <li> U+200E (left-to-right mark)
     * <li> U+200F (right-to-left mark)
     * <li> U+2028 (line separator)
     * <li> U+2029 (paragraph separator)
     *
     * @param c the character to check
     *
     * @return {@code true} if the character is whitespace, {@code false} otherwise
     */
    private static boolean isWhitespace(char c) {
        return c == '\t' || c == '\n' || c == '\u000B' || c == '\f' || c == '\r' || c == ' ' ||
            c == '\u0085' || c == '\u200E' || c == '\u200F' || c == '\u2028' || c == '\u2029';
    }

    private static TokenInfo parseNumber(
        String input, ParserPosition current
    ) {
        var start = new ParserPosition(current);

        while (current.position < input.length() &&
            Character.isDigit(input.charAt(current.position))) {
            ++current.position;
            ++current.column;
        }

        var number = input.substring(start.position, current.position);
        return new TokenInfo(new IntegerLiteral(Integer.parseInt(number)), new Position(start));
    }

    private static TokenInfo parseIdentifier(
        String input, ParserPosition current
    ) {
        var start = new ParserPosition(current);

        while (current.position < input.length() &&
            Character.isAlphabetic(input.charAt(current.position))) {
            ++current.position;
            ++current.column;
        }

        var identifier = input.substring(start.position, current.position);

        var keywordToken = KeywordsToken.fromString(identifier);
        return new TokenInfo(Objects.requireNonNullElseGet(keywordToken,
            () -> new IdentifierToken(identifier)
        ), new Position(start));
    }

    static class ParserPosition {

        int column = 0;
        int line = 0;
        int position = 0;

        public ParserPosition() {
        }

        public ParserPosition(ParserPosition other) {
            this.column = other.column;
            this.line = other.line;
            this.position = other.position;
        }
    }
}

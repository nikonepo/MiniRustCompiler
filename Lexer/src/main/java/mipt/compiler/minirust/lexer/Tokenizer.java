package mipt.compiler.minirust.lexer;

import static mipt.compiler.minirust.lexer.TokenInfo.EQ;
import static mipt.compiler.minirust.lexer.TokenInfo.EQEQ;
import static mipt.compiler.minirust.lexer.TokenInfo.IDENTIFIER;
import static mipt.compiler.minirust.lexer.TokenInfo.INTEGER_LITERAL;
import static mipt.compiler.minirust.lexer.TokenInfo.LEFT_CURLY_BRACKET;
import static mipt.compiler.minirust.lexer.TokenInfo.LEFT_PARENTHESES;
import static mipt.compiler.minirust.lexer.TokenInfo.MINUS;
import static mipt.compiler.minirust.lexer.TokenInfo.NE;
import static mipt.compiler.minirust.lexer.TokenInfo.NOT;
import static mipt.compiler.minirust.lexer.TokenInfo.PERCENT;
import static mipt.compiler.minirust.lexer.TokenInfo.PLUS;
import static mipt.compiler.minirust.lexer.TokenInfo.RIGHT_CURLY_BRACKET;
import static mipt.compiler.minirust.lexer.TokenInfo.RIGHT_PARENTHESES;
import static mipt.compiler.minirust.lexer.TokenInfo.SEMI;
import static mipt.compiler.minirust.lexer.TokenInfo.SLASH;
import static mipt.compiler.minirust.lexer.TokenInfo.STAR;

import java.util.ArrayList;
import java.util.List;
import mipt.compiler.minirust.lexer.tokens.keywords.KeywordType;

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
                    tokens.add(LEFT_CURLY_BRACKET(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '}':
                    tokens.add(RIGHT_CURLY_BRACKET(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '(':
                    tokens.add(LEFT_PARENTHESES(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case ')':
                    tokens.add(RIGHT_PARENTHESES(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                // Punctuations

                case '+':
                    tokens.add(PLUS(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '-':
                    tokens.add(MINUS(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '*':
                    tokens.add(STAR(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '/':
                    tokens.add(SLASH(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '%':
                    tokens.add(PERCENT(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

                    break;

                case '=':
                    if (currentPosition.position + 1 < input.length() &&
                        input.charAt(currentPosition.position + 1) == '=') {
                        tokens.add(EQEQ(new Position(currentPosition)));

                        currentPosition.column += 2;
                        currentPosition.position += 2;
                    } else {
                        tokens.add(EQ(new Position(currentPosition)));

                        ++currentPosition.column;
                        ++currentPosition.position;
                    }

                    break;

                case '!':
                    if (currentPosition.position + 1 < input.length() &&
                        input.charAt(currentPosition.position + 1) == '=') {
                        tokens.add(NE(new Position(currentPosition)));

                        currentPosition.column += 2;
                        currentPosition.position += 2;
                    } else {
                        tokens.add(NOT(new Position(currentPosition)));

                        ++currentPosition.column;
                        ++currentPosition.position;
                    }

                    break;

                case ';':
                    tokens.add(SEMI(new Position(currentPosition)));

                    ++currentPosition.column;
                    ++currentPosition.position;

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

        return INTEGER_LITERAL(new Position(start), Integer.parseInt(number));
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

        var keywordType = KeywordType.fromString(identifier);

        return keywordType == null ?
            IDENTIFIER(new Position(start), identifier) :
            new TokenInfo(keywordType.createToken(), new Position(start), new Position(current));
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

import static mipt.compiler.minirust.lexer.Position.of;
import static mipt.compiler.minirust.lexer.TokenInfo.EQ;
import static mipt.compiler.minirust.lexer.TokenInfo.EQEQ;
import static mipt.compiler.minirust.lexer.TokenInfo.FN;
import static mipt.compiler.minirust.lexer.TokenInfo.IDENTIFIER;
import static mipt.compiler.minirust.lexer.TokenInfo.INTEGER_LITERAL;
import static mipt.compiler.minirust.lexer.TokenInfo.LEFT_CURLY_BRACKET;
import static mipt.compiler.minirust.lexer.TokenInfo.LEFT_PARENTHESES;
import static mipt.compiler.minirust.lexer.TokenInfo.LET;
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
import static mipt.compiler.minirust.lexer.TokenInfo.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mipt.compiler.minirust.lexer.Position;
import mipt.compiler.minirust.lexer.TokenInfo;
import mipt.compiler.minirust.lexer.Tokenizer;
import mipt.compiler.minirust.lexer.tokens.DelimitersToken;
import mipt.compiler.minirust.lexer.tokens.KeywordsToken;
import mipt.compiler.minirust.lexer.tokens.PunctuationsToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TokenizerTest {

    @Test
    public void emptyToken() {
        var tokens = Tokenizer.tokenize("");

        assertEquals(0, tokens.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
    public void integerLiteralTest(int number) {
        var tokens = Tokenizer.tokenize("" + number);

        assertEquals(List.of(
            INTEGER_LITERAL(of(0, 0), number)
        ), tokens);
    }

    @Test
    public void keywordTest() {
        for (var keywordToken : KeywordsToken.values()) {
            var tokens = Tokenizer.tokenize(keywordToken.getWord());

            assertEquals(List.of(
                new TokenInfo(keywordToken, new Position(0, 0), new Position(keywordToken.getWord().length(), 0))
            ), tokens);
        }
    }

    // === Punctuations Tokens ===

    @Test
    public void simplePunctuationsTest() {
        for (var punctuationToken : PunctuationsToken.values()) {
            var tokens = Tokenizer.tokenize(punctuationToken.getSymbol());

            assertEquals(List.of(
                new TokenInfo(
                    punctuationToken,
                    new Position(0, 0),
                    new Position(punctuationToken.getSymbol().length(), 0)
                )
            ), tokens);
        }
    }

    @Test
    public void combinationPunctuationsTest() {
        var tokens = Tokenizer.tokenize(
            """
            = = ==
            != !
            + -
            /
            %
            """);

        assertEquals(List.of(
            EQ                      (of(0, 0)),
            EQ                      (of(2, 0)),
            EQEQ                    (of(4, 0)),

            NE                      (of(0, 1)),
            NOT                     (of(3, 1)),

            PLUS                    (of(0, 2)),
            MINUS                   (of(2, 2)),

            SLASH                   (of(0, 3)),

            PERCENT                 (of(0, 4))
        ), tokens);
    }

    // === Delimiters Tokens ===

    @Test
    public void simpleDelimitersTest() {
        for (var delimiterToken : DelimitersToken.values()) {
            var tokens = Tokenizer.tokenize(delimiterToken.getSymbol());

            assertEquals(List.of(
                new TokenInfo(
                    delimiterToken,
                    new Position(0, 0),
                    new Position(delimiterToken.getSymbol().length(), 0)
                )
            ), tokens);
        }
    }

    @Test
    public void combinationDelimitersTest() {
        var tokens = Tokenizer.tokenize(
            """
            { }
            ( )
            """);

        assertEquals(List.of(
            LEFT_CURLY_BRACKET      (of(0, 0)),
            RIGHT_CURLY_BRACKET     (of(2, 0)),

            LEFT_PARENTHESES        (of(0, 1)),
            RIGHT_PARENTHESES       (of(2, 1))
        ), tokens);
    }

    @Test
    public void combinationTokenTest() {
        {
            var tokens = Tokenizer.tokenize("a = 1+2-3*4/5%6!=7");

            assertEquals(List.of(
                IDENTIFIER          (of(0, 0), "a"),
                EQ                  (of(2, 0)),
                INTEGER_LITERAL     (of(4, 0), 1),
                PLUS                (of(5, 0)),
                INTEGER_LITERAL     (of(6, 0), 2),
                MINUS               (of(7, 0)),
                INTEGER_LITERAL     (of(8, 0), 3),
                STAR                (of(9, 0)),
                INTEGER_LITERAL     (of(10, 0), 4),
                SLASH               (of(11, 0)),
                INTEGER_LITERAL     (of(12, 0), 5),
                PERCENT             (of(13, 0)),
                INTEGER_LITERAL     (of(14, 0), 6),
                NE                  (of(15, 0)),
                INTEGER_LITERAL     (of(17, 0), 7)
            ), tokens);
        }
        {
            var tokens = Tokenizer.tokenize("""
                x = 10
                x = x + 1
                x = x - 2
                y = x * 3
                z = y / 4
                modVal = z % 5
                flag = !true
                isEqual = (x == y)
                notEqual = (x != y)
                """);

            assertEquals(List.of(
                IDENTIFIER          (of(0, 0), "x"),
                EQ                  (of(2, 0)),
                INTEGER_LITERAL     (of(4, 0), 10),

                IDENTIFIER          (of(0, 1), "x"),
                EQ                  (of(2, 1)),
                IDENTIFIER          (of(4, 1), "x"),
                PLUS                (of(6, 1)),
                INTEGER_LITERAL     (of(8, 1), 1),

                IDENTIFIER          (of(0, 2), "x"),
                EQ                  (of(2, 2)),
                IDENTIFIER          (of(4, 2), "x"),
                MINUS               (of(6, 2)),
                INTEGER_LITERAL     (of(8, 2), 2),

                IDENTIFIER          (of(0, 3), "y"),
                EQ                  (of(2, 3)),
                IDENTIFIER          (of(4, 3), "x"),
                STAR                (of(6, 3)),
                INTEGER_LITERAL     (of(8, 3), 3),

                IDENTIFIER          (of(0, 4), "z"),
                EQ                  (of(2, 4)),
                IDENTIFIER          (of(4, 4), "y"),
                SLASH               (of(6, 4)),
                INTEGER_LITERAL     (of(8, 4), 4),

                IDENTIFIER          (of(0, 5), "modVal"),
                EQ                  (of(7, 5)),
                IDENTIFIER          (of(9, 5), "z"),
                PERCENT             (of(11, 5)),
                INTEGER_LITERAL     (of(13, 5), 5),

                IDENTIFIER          (of(0, 6), "flag"),
                EQ                  (of(5, 6)),
                NOT                 (of(7, 6)),
                TRUE                (of(8, 6)),

                IDENTIFIER          (of(0, 7), "isEqual"),
                EQ                  (of(8, 7)),
                LEFT_PARENTHESES    (of(10, 7)),
                IDENTIFIER          (of(11, 7), "x"),
                EQEQ                (of(13, 7)),
                IDENTIFIER          (of(16, 7), "y"),
                RIGHT_PARENTHESES   (of(17, 7)),

                IDENTIFIER          (of(0, 8), "notEqual"),
                EQ                  (of(9, 8)),
                LEFT_PARENTHESES    (of(11, 8)),
                IDENTIFIER          (of(12, 8), "x"),
                NE                  (of(14, 8)),
                IDENTIFIER          (of(17, 8), "y"),
                RIGHT_PARENTHESES   (of(18, 8))
                ), tokens
            );
        }
    }

    @Test
    public void simpleProgram() {
        var tokens = Tokenizer.tokenize("""
            fn main() {
                let x = 10;
                let y = 20;
                let z = x + y;
            }
            """);

        assertEquals(List.of(
            FN                      (of(0, 0)),
            IDENTIFIER              (of(3, 0), "main"),
            LEFT_PARENTHESES        (of(7, 0)),
            RIGHT_PARENTHESES       (of(8, 0)),
            LEFT_CURLY_BRACKET      (of(10, 0)),

            LET                     (of(4, 1)),
            IDENTIFIER              (of(8, 1), "x"),
            EQ                      (of(10, 1)),
            INTEGER_LITERAL         (of(12, 1), 10),
            SEMI                    (of(14, 1)),

            LET                     (of(4, 2)),
            IDENTIFIER              (of(8, 2), "y"),
            EQ                      (of(10, 2)),
            INTEGER_LITERAL         (of(12, 2), 20),
            SEMI                    (of(14, 2)),

            LET                     (of(4, 3)),
            IDENTIFIER              (of(8, 3), "z"),
            EQ                      (of(10, 3)),
            IDENTIFIER              (of(12, 3), "x"),
            PLUS                    (of(14, 3)),
            IDENTIFIER              (of(16, 3), "y"),
            SEMI                    (of(17, 3)),

            RIGHT_CURLY_BRACKET     (of(0, 4))
        ), tokens);
    }
}

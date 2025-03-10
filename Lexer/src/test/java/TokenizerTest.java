import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mipt.compiler.minirust.lexer.Position;
import mipt.compiler.minirust.lexer.TokenInfo;
import mipt.compiler.minirust.lexer.Tokenizer;
import mipt.compiler.minirust.lexer.tokens.DelimitersToken;
import mipt.compiler.minirust.lexer.tokens.IdentifierToken;
import mipt.compiler.minirust.lexer.tokens.IntegerLiteral;
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

        assertEquals(List.of(new TokenInfo(new IntegerLiteral(number), new Position(0, 0))), tokens);
    }

    @Test
    public void keywordTest() {
        for (var keywordToken : KeywordsToken.values()) {
            var tokens = Tokenizer.tokenize(keywordToken.getWord());

            assertEquals(List.of(new TokenInfo(keywordToken, new Position(0, 0))), tokens);
        }
    }

    // === Punctuations Tokens ===

    @Test
    public void simplePunctuationsTest() {
        for (var punctuationToken : PunctuationsToken.values()) {
            var tokens = Tokenizer.tokenize(punctuationToken.getSymbol());

            assertEquals(List.of(new TokenInfo(punctuationToken, new Position(0, 0))), tokens);
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

        assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(0, 0)), tokens.get(0));
        assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 0)), tokens.get(1));
        assertEquals(new TokenInfo(PunctuationsToken.EQEQ, new Position(4, 0)), tokens.get(2));

        assertEquals(new TokenInfo(PunctuationsToken.NE, new Position(0, 1)), tokens.get(3));
        assertEquals(new TokenInfo(PunctuationsToken.NOT, new Position(3, 1)), tokens.get(4));

        assertEquals(new TokenInfo(PunctuationsToken.PLUS, new Position(0, 2)), tokens.get(5));
        assertEquals(new TokenInfo(PunctuationsToken.MINUS, new Position(2, 2)), tokens.get(6));

        assertEquals(new TokenInfo(PunctuationsToken.SLASH, new Position(0, 3)), tokens.get(7));
        assertEquals(new TokenInfo(PunctuationsToken.PERCENT, new Position(0, 4)), tokens.get(8));
    }

    // === Delimiters Tokens ===

    @Test
    public void simpleDelimitersTest() {
        for (var delimiterToken : DelimitersToken.values()) {
            var tokens = Tokenizer.tokenize(delimiterToken.getSymbol());

            assertEquals(List.of(new TokenInfo(delimiterToken, new Position(0, 0))), tokens);
        }
    }

    @Test
    public void combinationDelimitersTest() {
        var tokens = Tokenizer.tokenize(
            """
            { }
            ( )
            """);

        assertEquals(new TokenInfo(DelimitersToken.LEFT_CURLY_BRACKET, new Position(0, 0)), tokens.get(0));
        assertEquals(new TokenInfo(DelimitersToken.RIGHT_CURLY_BRACKET, new Position(2, 0)), tokens.get(1));

        assertEquals(new TokenInfo(DelimitersToken.LEFT_PARENTHESES, new Position(0, 1)), tokens.get(2));
        assertEquals(new TokenInfo(DelimitersToken.RIGHT_PARENTHESES, new Position(2, 1)), tokens.get(3));
    }

    @Test
    public void combinationTokenTest() {
        {
            var tokens = Tokenizer.tokenize("a = 1+2-3*4/5%6!=7");

            assertEquals(new TokenInfo(new IdentifierToken("a"), new Position(0, 0)), tokens.get(0));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 0)), tokens.get(1));
            assertEquals(new TokenInfo(new IntegerLiteral(1), new Position(4, 0)), tokens.get(2));
            assertEquals(new TokenInfo(PunctuationsToken.PLUS, new Position(5, 0)), tokens.get(3));
            assertEquals(new TokenInfo(new IntegerLiteral(2), new Position(6, 0)), tokens.get(4));
            assertEquals(new TokenInfo(PunctuationsToken.MINUS, new Position(7, 0)), tokens.get(5));
            assertEquals(new TokenInfo(new IntegerLiteral(3), new Position(8, 0)), tokens.get(6));
            assertEquals(new TokenInfo(PunctuationsToken.STAR, new Position(9, 0)), tokens.get(7));
            assertEquals(new TokenInfo(new IntegerLiteral(4), new Position(10, 0)), tokens.get(8));
            assertEquals(new TokenInfo(PunctuationsToken.SLASH, new Position(11, 0)),
                tokens.get(9));
            assertEquals(new TokenInfo(new IntegerLiteral(5), new Position(12, 0)), tokens.get(10));
            assertEquals(new TokenInfo(PunctuationsToken.PERCENT, new Position(13, 0)),
                tokens.get(11));
            assertEquals(new TokenInfo(new IntegerLiteral(6), new Position(14, 0)), tokens.get(12));
            assertEquals(new TokenInfo(PunctuationsToken.NE, new Position(15, 0)), tokens.get(13));
            assertEquals(new TokenInfo(new IntegerLiteral(7), new Position(17, 0)), tokens.get(14));
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

            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(0, 0)), tokens.get(0));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 0)), tokens.get(1));
            assertEquals(new TokenInfo(new IntegerLiteral(10), new Position(4, 0)), tokens.get(2));

            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(0, 1)), tokens.get(3));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 1)), tokens.get(4));
            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(4, 1)), tokens.get(5));
            assertEquals(new TokenInfo(PunctuationsToken.PLUS, new Position(6, 1)), tokens.get(6));
            assertEquals(new TokenInfo(new IntegerLiteral(1), new Position(8, 1)), tokens.get(7));

            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(0, 2)), tokens.get(8));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 2)), tokens.get(9));
            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(4, 2)), tokens.get(10));
            assertEquals(new TokenInfo(PunctuationsToken.MINUS, new Position(6, 2)), tokens.get(11));
            assertEquals(new TokenInfo(new IntegerLiteral(2), new Position(8, 2)), tokens.get(12));

            assertEquals(new TokenInfo(new IdentifierToken("y"), new Position(0, 3)), tokens.get(13));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 3)), tokens.get(14));
            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(4, 3)), tokens.get(15));
            assertEquals(new TokenInfo(PunctuationsToken.STAR, new Position(6, 3)), tokens.get(16));
            assertEquals(new TokenInfo(new IntegerLiteral(3), new Position(8, 3)), tokens.get(17));

            assertEquals(new TokenInfo(new IdentifierToken("z"), new Position(0, 4)), tokens.get(18));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(2, 4)), tokens.get(19));
            assertEquals(new TokenInfo(new IdentifierToken("y"), new Position(4, 4)), tokens.get(20));
            assertEquals(new TokenInfo(PunctuationsToken.SLASH, new Position(6, 4)), tokens.get(21));
            assertEquals(new TokenInfo(new IntegerLiteral(4), new Position(8, 4)), tokens.get(22));

            assertEquals(new TokenInfo(new IdentifierToken("modVal"), new Position(0, 5)), tokens.get(23));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(7, 5)), tokens.get(24));
            assertEquals(new TokenInfo(new IdentifierToken("z"), new Position(9, 5)), tokens.get(25));
            assertEquals(new TokenInfo(PunctuationsToken.PERCENT, new Position(11, 5)), tokens.get(26));
            assertEquals(new TokenInfo(new IntegerLiteral(5), new Position(13, 5)), tokens.get(27));

            assertEquals(new TokenInfo(new IdentifierToken("flag"), new Position(0, 6)), tokens.get(28));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(5, 6)), tokens.get(29));
            assertEquals(new TokenInfo(PunctuationsToken.NOT, new Position(7, 6)), tokens.get(30));
            assertEquals(new TokenInfo(KeywordsToken.TRUE, new Position(8, 6)), tokens.get(31));

            assertEquals(new TokenInfo(new IdentifierToken("isEqual"), new Position(0, 7)), tokens.get(32));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(8, 7)), tokens.get(33));
            assertEquals(new TokenInfo(DelimitersToken.LEFT_PARENTHESES, new Position(10, 7)), tokens.get(34));
            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(11, 7)), tokens.get(35));
            assertEquals(new TokenInfo(PunctuationsToken.EQEQ, new Position(13, 7)),
                tokens.get(36));
            assertEquals(new TokenInfo(new IdentifierToken("y"), new Position(16, 7)), tokens.get(37));
            assertEquals(new TokenInfo(DelimitersToken.RIGHT_PARENTHESES, new Position(17, 7)),
                tokens.get(38));

            assertEquals(new TokenInfo(new IdentifierToken("notEqual"), new Position(0, 8)), tokens.get(39));
            assertEquals(new TokenInfo(PunctuationsToken.EQ, new Position(9, 8)), tokens.get(40));
            assertEquals(new TokenInfo(DelimitersToken.LEFT_PARENTHESES, new Position(11, 8)),
                tokens.get(41));
            assertEquals(new TokenInfo(new IdentifierToken("x"), new Position(12, 8)), tokens.get(42));
            assertEquals(new TokenInfo(PunctuationsToken.NE, new Position(14, 8)), tokens.get(43));
            assertEquals(new TokenInfo(new IdentifierToken("y"), new Position(17, 8)), tokens.get(44));
            assertEquals(new TokenInfo(DelimitersToken.RIGHT_PARENTHESES, new Position(18, 8)),
                tokens.get(45));
        }
    }
}

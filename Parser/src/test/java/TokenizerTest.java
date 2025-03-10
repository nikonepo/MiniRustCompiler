import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mipt.compiler.minirust.parser.Position;
import mipt.compiler.minirust.parser.TokenInfo;
import mipt.compiler.minirust.parser.Tokenizer;
import mipt.compiler.minirust.parser.tokens.IntegerLiteral;
import mipt.compiler.minirust.parser.tokens.KeywordsToken;
import mipt.compiler.minirust.parser.tokens.PunctuationsToken;
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

        assertEquals(tokens, List.of(new TokenInfo(new IntegerLiteral(number), new Position(0, 0))));
    }

    @Test
    public void keywordTest() {
        for (var keywordToken : KeywordsToken.values()) {
            var tokens = Tokenizer.tokenize(keywordToken.getWord());

            assertEquals(tokens, List.of(new TokenInfo(keywordToken, new Position(0, 0))));
        }
    }

    // === Punctuations Tokens ===

    @Test
    public void simplePunctuationsTest() {
        for (var punctuationToken : PunctuationsToken.values()) {
            var tokens = Tokenizer.tokenize(punctuationToken.getSymbol());

            assertEquals(tokens, List.of(new TokenInfo(punctuationToken, new Position(0, 0))));
        }
    }
}

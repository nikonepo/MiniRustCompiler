import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import mipt.compiler.minirust.parser.Position;
import mipt.compiler.minirust.parser.TokenInfo;
import mipt.compiler.minirust.parser.Tokenizer;
import mipt.compiler.minirust.parser.tokens.IntegerLiteral;
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
    public void singleToken(int number) {
        var tokens = Tokenizer.tokenize("" + number);

        assertEquals(tokens, List.of(new TokenInfo(new IntegerLiteral(number), new Position(0, 0))));
    }
}

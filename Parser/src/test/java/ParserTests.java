import mipt.compiler.minirust.parser.internal.MiniRustLexer;
import mipt.compiler.minirust.parser.internal.MiniRustParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

public class ParserTests {
    @Test
    public void test1() {
        String inputText = "fn main() {let kek int = 100; print(kek);}";
        CharStream charStream = CharStreams.fromString(inputText);

        MiniRustLexer lexer = new MiniRustLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        MiniRustParser parser = new MiniRustParser(tokens);

        ParseTree tree = parser.program();

        System.out.println("Parse tree: " + tree.toStringTree(parser));
    }
}

import mipt.compiler.minirust.lexer.TokenInfo;
import mipt.compiler.minirust.lexer.Tokenizer;
import mipt.compiler.minirust.parser.rules.MiniRustParser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ListTokenSource;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class ParserTests {
    String inputText = "your text here";

    // Instantiate your lexer (Replace 'YourGrammarName' with your grammar's name)
    List<TokenInfo> tokens = Tokenizer.tokenize(inputText);

    // Create a token stream from the lexer
    CommonTokenStream tokens = new CommonTokenStream(new ListTokenSource(tokens));

    // Instantiate your parser
    MiniRustParser parser = new MiniRustParser(tokens);

    // Start parsing using a defined start rule (replace 'startRule' with your entry point)
    ParseTree tree = parser.expr();

    // Print the parsed tree
        System.out.println(tree.toStringTree(parser));
}

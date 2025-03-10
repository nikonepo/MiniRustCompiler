package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.lexer.TokenInfo;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class Parser
{
    private List<TokenInfo> tokens;

    public Parser(List<TokenInfo> tokens)
    {
        this.tokens = tokens;
    }

    public void parse()
    {
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create a parser with the token stream
        Parser parser = new ArithmeticParser(tokens);

        // Start parsing from the 'expr' rule
        ParseTree tree = parser.expr();
    }
}

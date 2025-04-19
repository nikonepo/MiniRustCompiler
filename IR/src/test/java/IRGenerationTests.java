import mipt.compiler.minirust.ir.IRTranslateVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustLexer;
import mipt.compiler.minirust.parser.internal.MiniRustParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

public class IRGenerationTests
{
    private static final String IR_OUTPUT = "D:\\GitReps\\MiniRustCompiler\\IR\\src\\test\\resources\\";

    @Test
    public void testOk1() throws IOException
    {
        doParseAndTranslateToIr("prog1.txt");
    }

    @Test
    public void testOk2() throws IOException
    {
        doParseAndTranslateToIr("prog2.txt");
    }

    private ParseTree doParse(String fileName, boolean printTree) throws IOException
    {
        MiniRustParser parser = null;
        ParseTree tree = null;

        try (var in = getClass().getClassLoader().getResourceAsStream(fileName))
        {
            assert in != null;
            String inputText = new String(in.readAllBytes());

            CharStream charStream = CharStreams.fromString(inputText);

            MiniRustLexer lexer = new MiniRustLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            parser = new MiniRustParser(tokens);
            tree = parser.program();
        }

        return tree;
    }

    private void doParseAndTranslateToIr(String fileName) throws IOException
    {
        var tree = doParse(fileName, true);

        try (PrintWriter writer = new PrintWriter(IR_OUTPUT + "IR" + fileName))
        {
            writer.println(new IRTranslateVisitor().visit(tree));
        }
    }
}

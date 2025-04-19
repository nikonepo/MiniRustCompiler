import mipt.compiler.minirust.parser.GraphvizVisitor;
import mipt.compiler.minirust.parser.SimpleInterpreter;
import mipt.compiler.minirust.parser.internal.MiniRustLexer;
import mipt.compiler.minirust.parser.internal.MiniRustParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParserTests
{
    private static final String DOT_FILE = "D:\\GitReps\\MiniRustCompiler\\Parser\\src\\test\\resources\\";

    @Test
    public void testOk1() throws IOException
    {
        doParseAndExec("prog1.txt");
    }

    @Test
    public void testOk2() throws IOException
    {
        doParseAndExec("prog5.txt");
    }

    @Test
    public void testFailed() throws IOException
    {
        // Есть ошибки при выводе по грамматике
        doParse("prog2.txt", true);

        // Ошибки времени исполнения
        Assertions.assertThrows(Exception.class, () -> doParseAndExec("prog3.txt"));
        Assertions.assertThrows(Exception.class, () -> doParseAndExec("prog4.txt"));
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

            if (printTree)
            {
                GraphvizVisitor gv = new GraphvizVisitor();
                String dot = gv.generateDot(tree);
                Files.writeString(Path.of(DOT_FILE + "tree.dot"), dot);
            }
        }

        return tree;
    }

    private void doParseAndExec(String fileName) throws IOException
    {
        var tree = doParse(fileName, true);

        new SimpleInterpreter().visit(tree);
    }
}

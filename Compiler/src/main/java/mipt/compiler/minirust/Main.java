package mipt.compiler.minirust;

import mipt.compiler.minirust.ir.IRTranslateVisitor;
import mipt.compiler.minirust.ir.ScopeVisitor;
import mipt.compiler.minirust.ir.TypeCheckVisitor;
import mipt.compiler.minirust.parser.GraphvizVisitor;
import mipt.compiler.minirust.parser.SimpleInterpreter;
import mipt.compiler.minirust.parser.internal.MiniRustLexer;
import mipt.compiler.minirust.parser.internal.MiniRustParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        String mode = args[0];
        File inputFile = new File(args[1]);

        switch (mode)
        {
            case "-ast":
            {
                File outputFile = new File(args[2]);
                drawAst(inputFile, outputFile);
                break;
            }
            case "-run":
            {
                interpretProgram(inputFile);
                break;
            }
            case "-ir":
            {
                File outputFile = new File(args[2]);
                generateIr(inputFile, outputFile);
                break;
            }
            case "-scope":
            {
                File outputFile = new File(args[2]);
                drawScopes(inputFile, outputFile);
                break;
            }
            case "-typecheck":
            {
                typeCheck(inputFile);
                break;
            }
            case "-compile":
            {
                File outputFile = new File(args[2]);
                String optimizationLevel = args.length > 3 ? args[3] : "0";
                compileToExecutable(inputFile, outputFile, optimizationLevel);
                break;
            }
            default:
            {
                System.out.println("Unknown mode: " + mode);
                break;
            }
        }
    }

    private static MiniRustParser.ProgramContext parseProgram(String code)
    {
        CharStream charStream = CharStreams.fromString(code);
        MiniRustLexer lexer = new MiniRustLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        var parser = new MiniRustParser(tokens);
        return parser.program();
    }

    private static void drawAst(File inputFile, File outputFile) throws IOException
    {
        var tree = parseProgram(Files.readString(inputFile.toPath()));
        GraphvizVisitor gv = new GraphvizVisitor();
        String dot = gv.generateDot(tree);

        Files.writeString(outputFile.toPath(), dot);
    }

    private static void interpretProgram(File inputFile) throws IOException
    {
        var tree = parseProgram(Files.readString(inputFile.toPath()));
        new SimpleInterpreter().visit(tree);
    }

    private static void generateIr(File inputFile, File outputFile) throws IOException
    {
        var tree = parseProgram(Files.readString(inputFile.toPath()));
        String result = new IRTranslateVisitor().visit(tree);

        Files.writeString(outputFile.toPath(), result);
    }

    private static void drawScopes(File inputFile, File outputFile) throws IOException
    {
        var tree = parseProgram(Files.readString(inputFile.toPath()));
        ScopeVisitor visitor = new ScopeVisitor();
        visitor.visit(tree);

        Files.writeString(outputFile.toPath(), visitor.generateDot());
    }

    private static void typeCheck(File inputFile) throws IOException
    {
        var tree = parseProgram(Files.readString(inputFile.toPath()));

        var scopeVisitor = new ScopeVisitor();
        scopeVisitor.visit(tree);

        var typeCheckVisitor = new TypeCheckVisitor(scopeVisitor);
        typeCheckVisitor.visit(tree);

        var errors = typeCheckVisitor.getErrors();
        if (errors.isEmpty()) {
            System.out.println("No type errors found.");
        } else {
            System.out.println("Found " + errors.size() + " type error(s):");
            for (String error : errors) {
                System.out.println("  - " + error);
            }
        }
    }

    private static void compileToExecutable(File inputFile, File outputFile, String optimizationLevel) throws IOException
    {
        // Generate IR
        var tree = parseProgram(Files.readString(inputFile.toPath()));
        var irVisitor = new IRTranslateVisitor();
        irVisitor.visit(tree);

        // Get the IR module
        var irModule = irVisitor.getModule();

        String irFileName = outputFile.getName().replaceFirst("[.][^.]+$", "") + ".ll";

        File irFile = new File(outputFile.getParentFile(), irFileName);

        // Translate IR to LLVM IR using the new visitor
        String llvmIR = new IRToLLVMVisitor().translate(irModule);
        Files.writeString(irFile.toPath(), llvmIR);

        try {
            LLVMCompiler.compile(irFile, outputFile, optimizationLevel);

            System.out.println("Compilation successful. Executable saved to " + outputFile.getAbsolutePath());
            System.out.println("LLVM IR saved to " + irFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Compilation failed. LLVM IR saved to " + irFile.getAbsolutePath());
            throw e;
        }
    }
}

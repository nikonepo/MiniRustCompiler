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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

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
        String customIR = irVisitor.visit(tree);

        String irFileName = outputFile.getName().replaceFirst("[.][^.]+$", "") + ".ll";

        File irFile = new File(outputFile.getParentFile(), irFileName);

//        String llvmIR =  convertToLLVMIR(customIR);
        Files.writeString(irFile.toPath(), customIR);

        try {
            // Compile the IR to an executable using LLVM/Clang API
            LLVMCompiler.compile(irFile, outputFile, optimizationLevel);

            System.out.println("Compilation successful. Executable saved to " + outputFile.getAbsolutePath());
            System.out.println("LLVM IR saved to " + irFile.getAbsolutePath());
        } catch (IOException e) {
            // If compilation fails, don't delete the IR file
            System.err.println("Compilation failed. LLVM IR saved to " + irFile.getAbsolutePath());
            throw e;
        }
    }
//
//    private static String convertToLLVMIR(String customIR)
//    {
//        // Get the target triple for the current system
//        String targetTriple = getTargetTriple();
//
//        // Convert the custom IR to standard LLVM IR
//        StringBuilder llvmIR = new StringBuilder();
//
//        // Add the target triple
//        llvmIR.append("target triple = \"").append(targetTriple).append("\"\n\n");
//
//        // Add declarations for external functions
//        llvmIR.append("declare i32 @printf(i8* nocapture readonly, ...)\n\n");
//
//        // Add string constants for print functions
//        llvmIR.append("@.str.int = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n");
//        llvmIR.append("@.str.bool = private unnamed_addr constant [4 x i8] c\"%s\\0A\\00\", align 1\n");
//        llvmIR.append("@.str.true = private unnamed_addr constant [5 x i8] c\"true\\00\", align 1\n");
//        llvmIR.append("@.str.false = private unnamed_addr constant [6 x i8] c\"false\\00\", align 1\n\n");
//
//        // Add print function implementations
//        llvmIR.append("define void @print_int(i32 %value) {\n");
//        llvmIR.append("  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.int, i64 0, i64 0), i32 %value)\n");
//        llvmIR.append("  ret void\n");
//        llvmIR.append("}\n\n");
//
//        llvmIR.append("define void @print_bool(i1 %value) {\n");
//        llvmIR.append("  %1 = select i1 %value, i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.true, i64 0, i64 0), i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.false, i64 0, i64 0)\n");
//        llvmIR.append("  %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.bool, i64 0, i64 0), i8* %1)\n");
//        llvmIR.append("  ret void\n");
//        llvmIR.append("}\n\n");
//
//        // Parse the custom IR and convert it to LLVM IR
//        String[] lines = customIR.split("\n");
//        StringBuilder currentFunction = new StringBuilder();
//        boolean inFunction = false;
//        boolean hasMainFunction = false;
//
//        for (String line : lines) {
//            line = line.trim();
//
//            // Skip empty lines
//            if (line.isEmpty()) {
//                continue;
//            }
//
//            // Check if this is the start of a function
//            if (line.startsWith("fn ") && line.endsWith("{")) {
//                inFunction = true;
//                String functionName = line.substring(3, line.indexOf("(")).trim();
//
//                // Check if this is the main function
//                if (functionName.equals("main")) {
//                    hasMainFunction = true;
//                    currentFunction.append("define i32 @main() {\n");
//                } else {
//                    currentFunction.append("define void @").append(functionName).append("() {\n");
//                }
//                continue;
//            }
//
//            // Check if this is the end of a function
//            if (line.equals("}")) {
//                if (inFunction) {
//                    // Add a return instruction if the function is main
//                    if (currentFunction.toString().contains("@main")) {
//                        currentFunction.append("  ret i32 0\n");
//                    } else {
//                        currentFunction.append("  ret void\n");
//                    }
//                    currentFunction.append("}\n\n");
//                    llvmIR.append(currentFunction);
//                    currentFunction = new StringBuilder();
//                    inFunction = false;
//                }
//                continue;
//            }
//
//            // Process instructions inside a function
//            if (inFunction) {
//                // Convert custom IR instructions to LLVM IR
//                if (line.contains("call print")) {
//                    // Handle print calls
//                    String arg = line.substring(line.lastIndexOf(",") + 1).trim();
//                    if (arg.startsWith("true") || arg.startsWith("false")) {
//                        // Boolean print
//                        boolean value = arg.startsWith("true");
//                        currentFunction.append("  call void @print_bool(i1 ").append(value ? "true" : "false").append(")\n");
//                    } else {
//                        // Integer print
//                        currentFunction.append("  call void @print_int(i32 ").append(arg).append(")\n");
//                    }
//                } else if (line.contains("store ")) {
//                    // Handle store instructions
//                    String[] parts = line.substring(6).split(",");
//                    String value = parts[0].trim();
//                    String target = parts[1].trim();
//                    currentFunction.append("  store i32 ").append(value).append(", i32* ").append(target).append("\n");
//                } else if (line.contains("load ")) {
//                    // Handle load instructions
//                    String[] parts = line.split("=");
//                    String target = parts[0].trim();
//                    String source = line.substring(line.indexOf("load ") + 5).trim();
//                    currentFunction.append("  ").append(target).append(" = load i32, i32* ").append(source).append("\n");
//                } else if (line.contains("create ")) {
//                    // Handle variable creation
//                    String[] parts = line.split(" ");
//                    String varName = parts[1].trim();
//                    String type = parts[2].trim();
//                    String llvmType = type.equals("int") ? "i32" : "i1";
//                    currentFunction.append("  ").append(varName).append(" = alloca ").append(llvmType).append("\n");
//                } else if (line.contains(" = ")) {
//                    // Handle operations (add, sub, mul, div, etc.)
//                    String[] parts = line.split("=");
//                    String target = parts[0].trim();
//                    String operation = parts[1].trim();
//
//                    if (operation.startsWith("add ")) {
//                        String[] operands = operation.substring(4).split(",");
//                        String left = operands[0].trim();
//                        String right = operands[1].trim();
//                        currentFunction.append("  ").append(target).append(" = add i32 ").append(left).append(", ").append(right).append("\n");
//                    } else if (operation.startsWith("sub ")) {
//                        String[] operands = operation.substring(4).split(",");
//                        String left = operands[0].trim();
//                        String right = operands[1].trim();
//                        currentFunction.append("  ").append(target).append(" = sub i32 ").append(left).append(", ").append(right).append("\n");
//                    } else if (operation.startsWith("mul ")) {
//                        String[] operands = operation.substring(4).split(",");
//                        String left = operands[0].trim();
//                        String right = operands[1].trim();
//                        currentFunction.append("  ").append(target).append(" = mul i32 ").append(left).append(", ").append(right).append("\n");
//                    } else if (operation.startsWith("div ")) {
//                        String[] operands = operation.substring(4).split(",");
//                        String left = operands[0].trim();
//                        String right = operands[1].trim();
//                        currentFunction.append("  ").append(target).append(" = sdiv i32 ").append(left).append(", ").append(right).append("\n");
//                    } else if (operation.startsWith("eq ") || operation.startsWith("ne ") ||
//                               operation.startsWith("lt ") || operation.startsWith("gt ") ||
//                               operation.startsWith("le ") || operation.startsWith("ge ")) {
//                        String op = operation.substring(0, 2);
//                        String[] operands = operation.substring(3).split(",");
//                        String left = operands[0].trim();
//                        String right = operands[1].trim();
//                        String icmpOp = "";
//                        switch (op) {
//                            case "eq": icmpOp = "eq"; break;
//                            case "ne": icmpOp = "ne"; break;
//                            case "lt": icmpOp = "slt"; break;
//                            case "gt": icmpOp = "sgt"; break;
//                            case "le": icmpOp = "sle"; break;
//                            case "ge": icmpOp = "sge"; break;
//                        }
//                        currentFunction.append("  ").append(target).append(" = icmp ").append(icmpOp).append(" i32 ")
//                                      .append(left).append(", ").append(right).append("\n");
//                    }
//                }
//            }
//        }
//
//        // If no main function was found, add a default one
//        if (!hasMainFunction) {
//            llvmIR.append("define i32 @main() {\n");
//            llvmIR.append("  call void @print_int(i32 42)\n");
//            llvmIR.append("  ret i32 0\n");
//            llvmIR.append("}\n");
//        }
//
//        return llvmIR.toString();
//    }

}

package mipt.compiler.minirust;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.llvm.global.LLVM;

/**
 * A class that handles compilation of LLVM IR to an executable file.
 * This implementation uses clang directly via subprocess calls.
 */
public class LLVMCompiler {

    static {
        // Initialize LLVM (needed for getTargetTriple)
        LLVM.LLVMInitializeAllTargetInfos();
        LLVM.LLVMInitializeAllTargets();
        LLVM.LLVMInitializeAllTargetMCs();
        LLVM.LLVMInitializeAllAsmParsers();
        LLVM.LLVMInitializeAllAsmPrinters();
    }

    /**
     * Compiles LLVM IR to an executable file using clang directly.
     *
     * @param irFile            The LLVM IR file
     * @param outputFile        The output executable file
     * @param optimizationLevel The optimization level (0-3)
     *
     * @throws IOException If an I/O error occurs
     */
    public static void compile(File irFile, File outputFile, String optimizationLevel)
        throws IOException {
        System.out.println("Compiling IR file: " + irFile.getAbsolutePath());
        System.out.println("Output file: " + outputFile.getAbsolutePath());
        System.out.println("Optimization level: " + optimizationLevel);

        Path outputDir = outputFile.toPath().getParent();
        if (outputDir != null && !Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        String[] command = {
            "clang", 
            "-O" + optimizationLevel, 
            "-o", 
            outputFile.getAbsolutePath(), 
            irFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            throw new IOException("Failed to compile LLVM IR file. Make sure clang is installed and in your PATH.");
        }

        outputFile.setExecutable(true);
        System.out.println("Created executable file: " + outputFile.getAbsolutePath());
    }

    /**
     * Gets the target triple for the current system.
     *
     * @return The target triple
     */
    public static String getTargetTriple() {
        BytePointer triplePtr = LLVM.LLVMGetDefaultTargetTriple();
        String triple = triplePtr.getString();
        LLVM.LLVMDisposeMessage(triplePtr);
        return triple;
    }

    /**
     * Executes a command.
     *
     * @param command The command to execute
     *
     * @return The exit code
     *
     * @throws IOException If an I/O error occurs
     */
    private static int executeCommand(String[] command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        try {
            return process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Command execution interrupted", e);
        }
    }
}

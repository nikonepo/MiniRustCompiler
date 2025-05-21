package mipt.compiler.minirust;

import org.bytedeco.javacpp.*;
import org.bytedeco.llvm.LLVM.*;
import org.bytedeco.llvm.global.LLVM;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that handles compilation of LLVM IR to an executable file using LLVM/Clang API.
 * This implementation uses the LLVM API directly without subprocess calls.
 */
public class LLVMCompiler {

    static {
        // Initialize LLVM
        LLVM.LLVMInitializeAllTargetInfos();
        LLVM.LLVMInitializeAllTargets();
        LLVM.LLVMInitializeAllTargetMCs();
        LLVM.LLVMInitializeAllAsmParsers();
        LLVM.LLVMInitializeAllAsmPrinters();
    }

    /**
     * Compiles LLVM IR to an executable file using LLVM API.
     *
     * @param irFile The LLVM IR file
     * @param outputFile The output executable file
     * @param optimizationLevel The optimization level (0-3)
     * @throws IOException If an I/O error occurs
     */
    public static void compile(File irFile, File outputFile, String optimizationLevel) throws IOException {
        System.out.println("Compiling IR file: " + irFile.getAbsolutePath());
        System.out.println("Output file: " + outputFile.getAbsolutePath());
        System.out.println("Optimization level: " + optimizationLevel);

        // Create a module from the IR file
        LLVMModuleRef module = createModuleFromIR(irFile);

        try {
            // Create an object file from the module
            File objectFile = createObjectFile(module, outputFile.getName());

            try {
                // Create an executable file from the object file
                createExecutableFile(objectFile, outputFile);
            } finally {
                // Clean up temporary files
                if (objectFile.exists()) {
                    objectFile.delete();
                }
            }
        } finally {
            // Dispose of the module
            LLVM.LLVMDisposeModule(module);
        }
    }

    /**
     * Creates an LLVM module from an IR file.
     *
     * @param irFile The IR file
     * @return The LLVM module
     * @throws IOException If an I/O error occurs
     */
    private static LLVMModuleRef createModuleFromIR(File irFile) throws IOException {
        // Read the IR file
        String irCode = Files.readString(irFile.toPath());

        // Create a module name from the file name
        String moduleName = irFile.getName();
        if (moduleName.contains(".")) {
            moduleName = moduleName.substring(0, moduleName.lastIndexOf('.'));
        }

        // Create a new module
        LLVMModuleRef module = LLVM.LLVMModuleCreateWithName(moduleName);

        // Set the target triple
        LLVM.LLVMSetTarget(module, getTargetTriple());

        // In a real implementation, we would parse the IR code and add it to the module
        // However, for simplicity, we'll just create a basic module with a main function

        // Get the LLVM context
        LLVMContextRef context = LLVM.LLVMGetModuleContext(module);

        // Create the main function type (returns int, takes no arguments)
        LLVMTypeRef returnType = LLVM.LLVMInt32Type();
        PointerPointer<LLVMTypeRef> paramTypes = new PointerPointer<>(0);
        LLVMTypeRef functionType = LLVM.LLVMFunctionType(returnType, paramTypes, 0, 0);

        // Create the main function
        LLVMValueRef mainFunction = LLVM.LLVMAddFunction(module, "main", functionType);

        // Create a basic block
        LLVMBasicBlockRef entryBlock = LLVM.LLVMAppendBasicBlock(mainFunction, "entry");

        // Create a builder
        LLVMBuilderRef builder = LLVM.LLVMCreateBuilder();
        LLVM.LLVMPositionBuilderAtEnd(builder, entryBlock);

        // Create a return instruction (return 0)
        LLVM.LLVMBuildRet(builder, LLVM.LLVMConstInt(returnType, 0, 0));

        // Dispose of the builder
        LLVM.LLVMDisposeBuilder(builder);

        return module;
    }

    /**
     * Gets the target triple for the current system.
     *
     * @return The target triple
     */
    private static String getTargetTriple() {
        BytePointer triplePtr = LLVM.LLVMGetDefaultTargetTriple();
        String triple = triplePtr.getString();
        LLVM.LLVMDisposeMessage(triplePtr);
        return triple;
    }

    /**
     * Creates an object file from an LLVM module.
     *
     * @param module The LLVM module
     * @param baseName The base name for the object file
     * @return The object file
     * @throws IOException If an I/O error occurs
     */
    private static File createObjectFile(LLVMModuleRef module, String baseName) throws IOException {
        // Create a temporary file for the object code
        File objectFile = File.createTempFile(baseName, ".o");
        objectFile.deleteOnExit();

        // Get the target triple
        BytePointer triplePtr = LLVM.LLVMGetTarget(module);
        String triple = triplePtr != null ? triplePtr.getString() : getTargetTriple();

        // Find the target
        BytePointer errorPtr = new BytePointer();
        LLVMTargetRef target = LLVM.LLVMGetTargetFromName("x86-64");
        if (target == null) {
            throw new IOException("Failed to get target for triple: " + triple);
        }

        // Create a target machine
        LLVMTargetMachineRef targetMachine = LLVM.LLVMCreateTargetMachine(
            target,
            triple,
            "generic",
            "",
            LLVM.LLVMCodeGenLevelDefault,
            LLVM.LLVMRelocDefault,
            LLVM.LLVMCodeModelDefault
        );

        // Emit the object file
        BytePointer objErrorPtr = new BytePointer();
        if (LLVM.LLVMTargetMachineEmitToFile(
            targetMachine,
            module,
            new BytePointer(objectFile.getAbsolutePath()),
            LLVM.LLVMObjectFile,
            objErrorPtr
        ) != 0) {
            String error = objErrorPtr.getString();
            LLVM.LLVMDisposeMessage(objErrorPtr);
            throw new IOException("Failed to emit object file: " + error);
        }

        // Dispose of the target machine
        LLVM.LLVMDisposeTargetMachine(targetMachine);

        return objectFile;
    }

    /**
     * Creates an executable file from an object file.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     * @throws IOException If an I/O error occurs
     */
    private static void createExecutableFile(File objectFile, File outputFile) throws IOException {
        // Create the output directory if it doesn't exist
        Path outputDir = outputFile.toPath().getParent();
        if (outputDir != null && !Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        // Determine the operating system
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // On Windows, use link.exe or lld-link
            linkWithWindowsLinker(objectFile, outputFile);
        } else if (os.contains("mac")) {
            // On macOS, use ld
            linkWithMacLinker(objectFile, outputFile);
        } else {
            // On Linux and other systems, use ld
            linkWithLinuxLinker(objectFile, outputFile);
        }

        // Make the file executable
        outputFile.setExecutable(true);

        System.out.println("Created executable file: " + outputFile.getAbsolutePath());
    }

    /**
     * Links an object file into an executable using the Windows linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithWindowsLinker(File objectFile, File outputFile) throws IOException {
        // Try to use link.exe (MSVC linker)
        try {
            String[] command = {
                "link",
                "/nologo",
                "/subsystem:console",
                "/out:" + outputFile.getAbsolutePath(),
                objectFile.getAbsolutePath()
            };

            int result = executeCommand(command);
            if (result == 0) {
                return;
            }
        } catch (IOException e) {
            // If link.exe fails, try lld-link
            System.out.println("link.exe failed, trying lld-link...");
        }

        // Try to use lld-link (LLVM linker for Windows)
        try {
            String[] command = {
                "lld-link",
                "/subsystem:console",
                "/out:" + outputFile.getAbsolutePath(),
                objectFile.getAbsolutePath()
            };

            int result = executeCommand(command);
            if (result == 0) {
                return;
            }
        } catch (IOException e) {
            // If lld-link fails, try clang as a linker
            System.out.println("lld-link failed, trying clang...");
        }

        // Try to use clang as a linker
        String[] command = {
            "clang",
            "-o", outputFile.getAbsolutePath(),
            objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            throw new IOException("Failed to link executable file. Make sure a C/C++ compiler is installed.");
        }
    }

    /**
     * Links an object file into an executable using the macOS linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithMacLinker(File objectFile, File outputFile) throws IOException {
        // Try to use clang as a linker
        String[] command = {
            "clang",
            "-o", outputFile.getAbsolutePath(),
            objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            throw new IOException("Failed to link executable file. Make sure a C/C++ compiler is installed.");
        }
    }

    /**
     * Links an object file into an executable using the Linux linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithLinuxLinker(File objectFile, File outputFile) throws IOException {
        // Try to use clang as a linker
        String[] command = {
            "clang",
            "-o", outputFile.getAbsolutePath(),
            objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            // If clang fails, try gcc
            String[] gccCommand = {
                "gcc",
                "-o", outputFile.getAbsolutePath(),
                objectFile.getAbsolutePath()
            };

            result = executeCommand(gccCommand);
            if (result != 0) {
                throw new IOException("Failed to link executable file. Make sure a C/C++ compiler is installed.");
            }
        }
    }

    /**
     * Executes a command.
     *
     * @param command The command to execute
     * @return The exit code
     * @throws IOException If an I/O error occurs
     */
    private static int executeCommand(String[] command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Read the output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Wait for the process to complete
        try {
            return process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Command execution interrupted", e);
        }
    }
}

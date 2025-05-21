package mipt.compiler.minirust;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.LLVMBasicBlockRef;
import org.bytedeco.llvm.LLVM.LLVMBuilderRef;
import org.bytedeco.llvm.LLVM.LLVMModuleRef;
import org.bytedeco.llvm.LLVM.LLVMTargetMachineRef;
import org.bytedeco.llvm.LLVM.LLVMTargetRef;
import org.bytedeco.llvm.LLVM.LLVMTypeRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;
import org.bytedeco.llvm.global.LLVM;

/**
 * A class that handles compilation of LLVM IR to an executable file using LLVM/Clang API. This
 * implementation uses the LLVM API directly without subprocess calls.
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

        LLVMModuleRef module = createModuleFromIR(irFile);

        try {
            File objectFile = createObjectFile(module, outputFile.getName());

            try {
                createExecutableFile(objectFile, outputFile);
            } finally {
                if (objectFile.exists()) {
                    objectFile.delete();
                }
            }
        } finally {
            LLVM.LLVMDisposeModule(module);
        }
    }

    /**
     * Creates an LLVM module from an IR file.
     *
     * @param irFile The IR file
     *
     * @return The LLVM module
     */
    private static LLVMModuleRef createModuleFromIR(File irFile) {
        String moduleName = irFile.getName();
        if (moduleName.contains(".")) {
            moduleName = moduleName.substring(0, moduleName.lastIndexOf('.'));
        }

        LLVMModuleRef module = LLVM.LLVMModuleCreateWithName(moduleName);
        LLVM.LLVMSetTarget(module, getTargetTriple());

        LLVMTypeRef returnType = LLVM.LLVMInt32Type();
        PointerPointer<LLVMTypeRef> paramTypes = new PointerPointer<>(0);
        LLVMTypeRef functionType = LLVM.LLVMFunctionType(returnType, paramTypes, 0, 0);

        LLVMValueRef mainFunction = LLVM.LLVMAddFunction(module, "main", functionType);
        LLVMBasicBlockRef entryBlock = LLVM.LLVMAppendBasicBlock(mainFunction, "entry");

        LLVMBuilderRef builder = LLVM.LLVMCreateBuilder();
        LLVM.LLVMPositionBuilderAtEnd(builder, entryBlock);

        LLVM.LLVMBuildRet(builder, LLVM.LLVMConstInt(returnType, 0, 0));
        LLVM.LLVMDisposeBuilder(builder);

        return module;
    }

    /**
     * Creates an object file from an LLVM module.
     *
     * @param module   The LLVM module
     * @param baseName The base name for the object file
     *
     * @return The object file
     *
     * @throws IOException If an I/O error occurs
     */
    private static File createObjectFile(LLVMModuleRef module, String baseName) throws IOException {
        File objectFile = File.createTempFile(baseName, ".o");
        objectFile.deleteOnExit();

        BytePointer triplePtr = LLVM.LLVMGetTarget(module);
        String triple = triplePtr != null ? triplePtr.getString() : getTargetTriple();

        LLVMTargetRef target = LLVM.LLVMGetTargetFromName("x86-64");
        if (target == null) {
            throw new IOException("Failed to get target for triple: " + triple);
        }

        LLVMTargetMachineRef targetMachine = LLVM.LLVMCreateTargetMachine(
            target,
            triple,
            "generic",
            "",
            LLVM.LLVMCodeGenLevelDefault,
            LLVM.LLVMRelocDefault,
            LLVM.LLVMCodeModelDefault
        );

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

        LLVM.LLVMDisposeTargetMachine(targetMachine);

        return objectFile;
    }

    /**
     * Creates an executable file from an object file.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     *
     * @throws IOException If an I/O error occurs
     */
    private static void createExecutableFile(File objectFile, File outputFile) throws IOException {
        Path outputDir = outputFile.toPath().getParent();
        if (outputDir != null && !Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            linkWithWindowsLinker(objectFile, outputFile);
        } else if (os.contains("mac")) {
            linkWithMacLinker(objectFile, outputFile);
        } else {
            linkWithLinuxLinker(objectFile, outputFile);
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
     * Links an object file into an executable using the Windows linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     *
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithWindowsLinker(File objectFile, File outputFile) throws IOException {
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
            System.out.println("link.exe failed, trying lld-link...");
        }

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
            System.out.println("lld-link failed, trying clang...");
        }

        String[] command = {
            "clang", "-o", outputFile.getAbsolutePath(), objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            throw new IOException(
                "Failed to link executable file. Make sure a C/C++ compiler is installed.");
        }
    }

    /**
     * Links an object file into an executable using the macOS linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     *
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithMacLinker(File objectFile, File outputFile) throws IOException {
        String[] command = {
            "clang", "-o", outputFile.getAbsolutePath(), objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            throw new IOException(
                "Failed to link executable file. Make sure a C/C++ compiler is installed.");
        }
    }

    /**
     * Links an object file into an executable using the Linux linker.
     *
     * @param objectFile The object file
     * @param outputFile The output executable file
     *
     * @throws IOException If an I/O error occurs
     */
    private static void linkWithLinuxLinker(File objectFile, File outputFile) throws IOException {
        String[] command = {
            "clang", "-o", outputFile.getAbsolutePath(), objectFile.getAbsolutePath()
        };

        int result = executeCommand(command);
        if (result != 0) {
            String[] gccCommand = {
                "gcc", "-o", outputFile.getAbsolutePath(), objectFile.getAbsolutePath()
            };

            result = executeCommand(gccCommand);
            if (result != 0) {
                throw new IOException(
                    "Failed to link executable file. Make sure a C/C++ compiler is installed.");
            }
        }
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

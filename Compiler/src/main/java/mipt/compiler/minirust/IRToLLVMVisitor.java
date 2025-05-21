package mipt.compiler.minirust;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import mipt.compiler.minirust.ir.IRVisitor;
import mipt.compiler.minirust.ir.internal.IRCall;
import mipt.compiler.minirust.ir.internal.IRCondJump;
import mipt.compiler.minirust.ir.internal.IRCreate;
import mipt.compiler.minirust.ir.internal.IREscape;
import mipt.compiler.minirust.ir.internal.IRFunction;
import mipt.compiler.minirust.ir.internal.IRInstruction;
import mipt.compiler.minirust.ir.internal.IRJump;
import mipt.compiler.minirust.ir.internal.IRLabel;
import mipt.compiler.minirust.ir.internal.IRLoad;
import mipt.compiler.minirust.ir.internal.IRModule;
import mipt.compiler.minirust.ir.internal.IROperation;
import mipt.compiler.minirust.ir.internal.IROperation.Op;
import mipt.compiler.minirust.ir.internal.IRStore;
import mipt.compiler.minirust.ir.internal.IRValue;

/**
 * Visitor that translates IR instructions to LLVM IR.
 */
public class IRToLLVMVisitor implements IRVisitor<String> {

    private final Set<String>         declaredFunctions = new HashSet<>();
    private final StringBuilder       llvmIR            = new StringBuilder();
    private final Map<String, String> variableTypes     = new HashMap<>();

    /**
     * Translates an IR module to LLVM IR.
     *
     * @param module The IR module
     *
     * @return The LLVM IR code
     */
    public String translate(IRModule module) {
        // Reset state
        llvmIR.setLength(0);
        variableTypes.clear();
        declaredFunctions.clear();

        // Add target triple
        llvmIR.append("target triple = \"").append(LLVMCompiler.getTargetTriple()).append("\"\n\n");

        // Add declarations for external functions
        llvmIR.append("declare i32 @printf(i8* nocapture readonly, ...)\n\n");

        // Add string constants for print functions
        llvmIR.append(
            "@.str.int = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n");
        llvmIR.append(
            "@.str.bool = private unnamed_addr constant [4 x i8] c\"%s\\0A\\00\", align 1\n");
        llvmIR.append("@.str.true = private unnamed_addr constant [5 x i8] c\"true\\00\", align 1\n");
        llvmIR.append(
            "@.str.false = private unnamed_addr constant [6 x i8] c\"false\\00\", align 1\n\n");

        llvmIR.append("define void @print_int(i32 %value) {\n");
        llvmIR.append(
            "  %1 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.int, i64 0, i64 0), i32 %value)\n");
        llvmIR.append("  ret void\n");
        llvmIR.append("}\n\n");

        llvmIR.append("define void @print_bool(i1 %value) {\n");
        llvmIR.append(
            "  %1 = select i1 %value, i8* getelementptr inbounds ([5 x i8], [5 x i8]* @.str.true, i64 0, i64 0), i8* getelementptr inbounds ([6 x i8], [6 x i8]* @.str.false, i64 0, i64 0)\n");
        llvmIR.append(
            "  %2 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.str.bool, i64 0, i64 0), i8* %1)\n");
        llvmIR.append("  ret void\n");
        llvmIR.append("}\n\n");

        visitModule(module);

        return llvmIR.toString();
    }

    @Override
    public String visit(IRInstruction instruction) {
        if (instruction instanceof IRCreate) {
            return visitCreate((IRCreate) instruction);
        } else if (instruction instanceof IRStore) {
            return visitStore((IRStore) instruction);
        } else if (instruction instanceof IRLoad) {
            return visitLoad((IRLoad) instruction);
        } else if (instruction instanceof IROperation) {
            return visitOperation((IROperation) instruction);
        } else if (instruction instanceof IRCall) {
            return visitCall((IRCall) instruction);
        } else if (instruction instanceof IRCondJump) {
            return visitCondJump((IRCondJump) instruction);
        } else if (instruction instanceof IRJump) {
            return visitJump((IRJump) instruction);
        } else if (instruction instanceof IRLabel) {
            return visitLabel((IRLabel) instruction);
        } else if (instruction instanceof IRValue) {
            return visitValue((IRValue) instruction);
        } else if (instruction instanceof IREscape) {
            return visitEscape((IREscape) instruction);
        } else {
            return "; Unknown instruction: " + instruction;
        }
    }

    @Override
    public String visitModule(IRModule module) {
        boolean hasMainFunction = false;
        for (IRFunction function : module.functions) {
            if (function.name.equals("main")) {
                hasMainFunction = true;
            }
            visitFunction(function);
        }

        if (!hasMainFunction) {
            llvmIR.append("define i32 @main() {\n");
            llvmIR.append("  ret i32 0\n");
            llvmIR.append("}\n");
        }

        return null;
    }

    @Override
    public String visitFunction(IRFunction function) {
        declaredFunctions.add(function.name);

        if (function.name.equals("main")) {
            llvmIR.append("define i32 @main() {\n");
        } else {
            llvmIR.append("define i32 @").append(function.name).append("() {\n");
        }

        for (IRInstruction instr : function.instructions) {
            String llvmInst = visit(instr);
            if (llvmInst == null || llvmInst.isEmpty()) {
                continue;
            }

            llvmIR.append("  ").append(llvmInst).append("\n");
        }

        if (function.name.equals("main")) {
            llvmIR.append("  ret i32 0\n");
        } else {
            llvmIR.append("  ret i32 0\n");
        }

        llvmIR.append("}\n\n");

        return null;
    }

    @Override
    public String visitCreate(IRCreate instruction) {
        String variable = instruction.target;
        String type = instruction.type;

        variableTypes.put(variable, type);
        String varName = ensurePrefix(variable);
        return String.format("%s = alloca %s", varName, getLLVMType(type));
    }

    @Override
    public String visitStore(IRStore instruction) {
        String value = instruction.value;
        String target = instruction.target;

        if (value.matches("-?\\d+")) {
            String targetVar = ensurePrefix(target);
            return "store i32 " + value + ", i32* " + targetVar;
        } else if (value.equals("true") || value.equals("false")) {
            String targetVar = ensurePrefix(target);
            return "store i1 " + (value.equals("true") ? "1" : "0") + ", i1* " + targetVar;
        } else {
            String type = variableTypes.getOrDefault(value, "int");
            String valueVar = ensurePrefix(value);
            String targetVar = ensurePrefix(target);
            return "store " + getLLVMType(type) + " " + valueVar + ", " + getLLVMType(type) + "* " +
                targetVar;
        }
    }

    @Override
    public String visitLoad(IRLoad instruction) {
        String target = instruction.target;
        String source = instruction.source;

        String type = variableTypes.getOrDefault(source, "int");
        String targetVar = ensurePrefix(target);
        String sourceVar = ensurePrefix(source);
        return targetVar + " = load " + getLLVMType(type) + ", " + getLLVMType(type) + "* " +
            sourceVar;
    }

    @Override
    public String visitOperation(IROperation instruction) {
        String target = instruction.target;
        String left = instruction.left;
        String right = instruction.right;
        Op operation = instruction.op;

        String llvmOp = getLLVMOperation(operation);

        String type = "i32";
        String resultType = "i32";
        if (operation == Op.EQ || operation == Op.NE || operation == Op.LT || operation == Op.GT ||
            operation == Op.LE || operation == Op.GE) {
            resultType = "i1";
        } else if (operation == Op.AND || operation == Op.OR) {
            type = "i1";
            resultType = "i1";
        }

        left = convertOperand(left);
        right = convertOperand(right);
        String targetVar = ensurePrefix(target);

        if (operation == Op.EQ || operation == Op.NE || operation == Op.LT || operation == Op.GT ||
            operation == Op.LE || operation == Op.GE) {
            return targetVar + " = " + llvmOp + " " + type + " " + left + ", " + right;
        } else {
            return targetVar + " = " + llvmOp + " " + resultType + " " + left + ", " + right;
        }
    }

    @Override
    public String visitCall(IRCall instruction) {
        String target = instruction.target;
        String function = instruction.function;

        if (function.equals("print") && !instruction.args.isEmpty()) {
            String arg = instruction.args.get(0);

            if (arg.matches("-?\\d+")) {
                return "call void @print_int(i32 " + arg + ")";
            } else if (arg.equals("true") || arg.equals("false")) {
                return "call void @print_bool(i1 " + (arg.equals("true") ? "1" : "0") + ")";
            } else {
                String type = variableTypes.getOrDefault(arg, "int");
                if (type.equals("bool")) {
                    String argVar = ensurePrefix(arg);
                    return "call void @print_bool(i1 " + argVar + ")";
                } else {
                    String argVar = ensurePrefix(arg);
                    return "call void @print_int(i32 " + argVar + ")";
                }
            }
        } else if (declaredFunctions.contains(function)) {
            if (target != null && !target.isEmpty()) {
                String targetVar = ensurePrefix(target);
                return targetVar + " = call i32 @" + function + "()";
            } else {
                return "call i32 @" + function + "()";
            }
        }

        return "; Unknown call: " + instruction;
    }

    @Override
    public String visitCondJump(IRCondJump instruction) {
        String condition = instruction.condition;
        String label = instruction.label;
        String endLabel = instruction.endLabel;

        condition = convertOperand(condition);

        return "br i1 " + condition + ", label %" + label + ", label %" + endLabel;
    }

    @Override
    public String visitJump(IRJump instruction) {
        String label = instruction.label;
        return "br label %" + label;
    }

    @Override
    public String visitLabel(IRLabel instruction) {
        String lbl = instruction.name + ":";
        llvmIR.append(lbl).append("\n");
        return null;
    }

    @Override
    public String visitValue(IRValue instruction) {
        return "; Value: " + instruction;
    }

    @Override
    public String visitEscape(IREscape instruction) {
        return "; Escape: " + instruction;
    }

    /**
     * Gets the LLVM type for a custom IR type.
     *
     * @param type The custom IR type
     *
     * @return The LLVM type
     */
    private String getLLVMType(String type) {
        return switch (type) {
            case "int" -> "i32";
            case "bool" -> "i1";
            default -> "i32";
        };
    }

    /**
     * Gets the LLVM operation for a custom IR operation.
     *
     * @param operation The custom IR operation
     *
     * @return The LLVM operation
     */
    private String getLLVMOperation(Op operation) {
        return switch (operation) {
            case ADD -> "add";
            case SUB -> "sub";
            case MUL -> "mul";
            case DIV -> "sdiv";
            case EQ -> "icmp eq";
            case NE -> "icmp ne";
            case LT -> "icmp slt";
            case GT -> "icmp sgt";
            case LE -> "icmp sle";
            case GE -> "icmp sge";
            case AND -> "and";
            case OR -> "or";
        };
    }

    /**
     * Converts an operand to LLVM IR format.
     *
     * @param operand The operand
     *
     * @return The LLVM IR operand
     */
    private String convertOperand(String operand) {
        // Check if the operand is a literal integer
        if (operand.matches("-?\\d+")) {
            return operand;
        }
        // Check if the operand is a literal boolean
        else if (operand.equals("true") || operand.equals("false")) {
            return operand.equals("true") ? "1" : "0";
        }
        // Otherwise, assume it's a variable
        else {
            return ensurePrefix(operand);
        }
    }

    /**
     * Ensures that a variable name has the % prefix.
     *
     * @param name The variable name
     *
     * @return The variable name with the % prefix
     */
    private String ensurePrefix(String name) {
        if (!name.startsWith("%")) {
            return "%" + name;
        }
        return name;
    }
}

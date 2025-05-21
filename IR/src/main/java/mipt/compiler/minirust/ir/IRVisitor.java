package mipt.compiler.minirust.ir;

import mipt.compiler.minirust.ir.internal.*;

/**
 * Interface for visitors that process IR instructions.
 */
public interface IRVisitor<T> {
    T visit(IRInstruction instruction);
    T visitModule(IRModule module);
    T visitFunction(IRFunction function);
    T visitCreate(IRCreate instruction);
    T visitStore(IRStore instruction);
    T visitLoad(IRLoad instruction);
    T visitOperation(IROperation instruction);
    T visitCall(IRCall instruction);
    T visitCondJump(IRCondJump instruction);
    T visitJump(IRJump instruction);
    T visitLabel(IRLabel instruction);
    T visitValue(IRValue instruction);
    T visitEscape(IREscape instruction);
}
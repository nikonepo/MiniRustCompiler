package mipt.compiler.minirust.ir.internal;

public class IROperation extends IRInstruction
{
    public enum Op
    {
        ADD, SUB, MUL, DIV, EQ, NE, LT, GT, LE, GE, AND, OR
    }

    public String target;
    public String left;
    public String right;
    public Op op;

    public IROperation(String target, String left, String right, Op op)
    {
        this.target = target;
        this.left = left;
        this.right = right;
        this.op = op;
    }

    @Override
    public String toString()
    {
        return target + " = " + op.name().toLowerCase() + " " + left + ", " + right;
    }
}


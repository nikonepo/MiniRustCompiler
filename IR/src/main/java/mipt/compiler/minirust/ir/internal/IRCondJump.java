package mipt.compiler.minirust.ir.internal;

public class IRCondJump extends IRInstruction
{
    public String condition;
    public String label;
    public String endLabel;

    public IRCondJump(String condition, String label, String endLabel)
    {
        this.condition = condition;
        this.label = label;
        this.endLabel = endLabel;
    }

    @Override
    public String toString()
    {
        return "cond_jump " + condition + ", " + label + ", " + endLabel;
    }
}

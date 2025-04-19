package mipt.compiler.minirust.ir.internal;

public class IRCondJump extends IRInstruction
{
    public String condition;
    public String label;

    public IRCondJump(String condition, String label)
    {
        this.condition = condition;
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "cond_jump " + condition + ", " + label;
    }
}

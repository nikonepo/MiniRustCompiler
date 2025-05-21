package mipt.compiler.minirust.ir.internal;

public class IRJump extends IRInstruction
{
    public String label;

    public IRJump(String label)
    {
        this.label = label;
    }

    @Override
    public String toString()
    {
        return "jump " + label;
    }
}

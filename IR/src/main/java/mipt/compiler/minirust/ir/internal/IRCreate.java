package mipt.compiler.minirust.ir.internal;

public class IRCreate extends IRInstruction
{
    public String target;
    public String type;

    public IRCreate(String target, String type)
    {
        this.target = target;
        this.type = type;
    }

    @Override
    public String toString()
    {
        return target + " = create " + type;
    }
}
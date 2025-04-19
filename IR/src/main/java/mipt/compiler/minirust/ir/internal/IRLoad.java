package mipt.compiler.minirust.ir.internal;

public class IRLoad extends IRInstruction
{
    public String target;
    public String source;

    public IRLoad(String target, String source)
    {
        this.target = target;
        this.source = source;
    }

    @Override
    public String toString()
    {
        return target + " = load " + source;
    }
}
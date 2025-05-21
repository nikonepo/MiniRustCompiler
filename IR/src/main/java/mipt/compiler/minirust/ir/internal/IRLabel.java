package mipt.compiler.minirust.ir.internal;

public class IRLabel extends IRInstruction
{
    public String name;

    public IRLabel(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "label " + name;
    }
}
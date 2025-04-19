package mipt.compiler.minirust.ir.internal;

public class IRStore extends IRInstruction
{
    public String value;
    public String target;

    public IRStore(String value, String target)
    {
        this.value = value;
        this.target = target;
    }

    @Override
    public String toString()
    {
        return "store " + value + ", " + target;
    }
}
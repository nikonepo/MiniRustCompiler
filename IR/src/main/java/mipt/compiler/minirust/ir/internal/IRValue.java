package mipt.compiler.minirust.ir.internal;

public class IRValue extends IRInstruction
{
    public enum Type
    {
        INT, FLOAT, BOOL
    }

    public final Type type;
    public final Object value;

    public IRValue(Type type, Object value)
    {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return switch (type)
        {
            case INT -> "VALUE int " + value;
            case FLOAT -> "VALUE float " + value;
            case BOOL -> "VALUE bool " + value;
        };
    }
}
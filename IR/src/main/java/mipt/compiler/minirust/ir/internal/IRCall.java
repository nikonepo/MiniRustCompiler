package mipt.compiler.minirust.ir.internal;

import java.util.List;

public class IRCall extends IRInstruction
{
    public String target;
    public String function;
    public List<String> args;

    public IRCall(String target, String function, List<String> args)
    {
        this.target = target;
        this.function = function;
        this.args = args;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(target != null ? target + " = " : "").append("call ").append(function);
        if (function.equals("print"))
        {
            sb.append(", ").append(String.join(", ", args));
        }

        return sb.toString();
    }
}
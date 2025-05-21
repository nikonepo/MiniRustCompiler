package mipt.compiler.minirust.ir.internal;

import java.util.ArrayList;
import java.util.List;

public class IRFunction
{
    public String name;
    public List<IRInstruction> instructions = new ArrayList<>();

    public IRFunction(String name)
    {
        this.name = name;
    }

    public void add(IRInstruction instr)
    {
        instructions.add(instr);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("fn ").append(name).append("() {\n");
        for (IRInstruction instr : instructions)
        {
            sb.append("  ").append(instr).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
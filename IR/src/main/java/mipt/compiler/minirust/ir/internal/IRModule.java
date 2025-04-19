package mipt.compiler.minirust.ir.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IRModule
{
    public List<IRFunction> functions = new ArrayList<>();

    public void addFunction(IRFunction function)
    {
        functions.add(function);
    }

    @Override
    public String toString()
    {
        return functions.stream()
                .map(IRFunction::toString)
                .collect(Collectors.joining("\n\n"));
    }
}
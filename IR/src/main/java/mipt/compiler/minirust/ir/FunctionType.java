package mipt.compiler.minirust.ir;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a function type in the MiniRust language.
 */
public class FunctionType {

    private final String              name;
    private final List<ParameterType> parameters;
    private final Type                returnType;

    public FunctionType(String name, List<ParameterType> parameters, Type returnType) {
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public List<ParameterType> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean checkArgumentTypes(List<Type> argumentTypes) {
        if (argumentTypes.size() != parameters.size()) {
            return false;
        }

        for (int i = 0; i < parameters.size(); i++) {
            if (!parameters.get(i).getType().equals(argumentTypes.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        String params = parameters.stream()
            .map(ParameterType::toString)
            .collect(Collectors.joining(", "));
        return name + "(" + params + ")";
    }

    /**
     * Represents a parameter type in a function.
     */
    public static class ParameterType {

        private final String name;
        private final Type   type;

        public ParameterType(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return name + ": " + type.getName();
        }
    }
}
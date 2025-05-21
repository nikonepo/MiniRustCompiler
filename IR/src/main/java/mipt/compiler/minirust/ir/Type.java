package mipt.compiler.minirust.ir;

/**
 * Represents a type in the MiniRust language.
 */
public class Type {

    private final TypeKind     kind;
    private final String       name;
    private       FunctionType functionType;
    public Type(TypeKind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public TypeKind getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public boolean isError() {
        return kind == TypeKind.ERROR;
    }

    public boolean isPrimitive() {
        return kind == TypeKind.INT || kind == TypeKind.BOOL || kind == TypeKind.FLOAT;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Type other = (Type) obj;
        return kind == other.kind;
    }

    @Override
    public String toString() {
        if (isFunction() && functionType != null) {
            return functionType.toString();
        }
        return name;
    }

    public boolean isFunction() {
        return kind == TypeKind.FUNCTION;
    }

    public static Type intType() {
        return new Type(TypeKind.INT, "int");
    }

    public static Type boolType() {
        return new Type(TypeKind.BOOL, "bool");
    }

    public static Type floatType() {
        return new Type(TypeKind.FLOAT, "float");
    }

    public static Type functionType(String name, FunctionType functionType) {
        Type type = new Type(TypeKind.FUNCTION, name);
        type.functionType = functionType;
        return type;
    }

    public static Type errorType() {
        return new Type(TypeKind.ERROR, "error");
    }

    public enum TypeKind {
        INT, BOOL, FLOAT, FUNCTION, ERROR
    }
}
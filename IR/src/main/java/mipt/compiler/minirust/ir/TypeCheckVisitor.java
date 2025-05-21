package mipt.compiler.minirust.ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mipt.compiler.minirust.ir.FunctionType.ParameterType;
import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser.ArithmeticExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.AssignmentContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.BlockContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.ComparisonExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.ExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.ExpressionStatementContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.FactorContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.FunctionCallContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.FunctionDeclarationContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.IdentifierExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.IfStatementContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.LetStatementContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.LiteralExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.LogicalExpressionContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.LoopStatementContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.PrintStatementContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.ProgramContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.TermContext;
import mipt.compiler.minirust.parser.internal.MiniRustParser.WhileStatementContext;

/**
 * A visitor that performs type checking on the AST.
 * It traverses the scope tree and checks for type
 * errors.
 */
public class TypeCheckVisitor extends MiniRustBaseVisitor<Type> {

    private final List<String>              errors        = new ArrayList<>();
    private final Map<String, FunctionType> functionTypes = new HashMap<>();
    private final ScopeVisitor.Scope        globalScope;
    private final Map<String, Type>         variableTypes = new HashMap<>();
    private       ScopeVisitor.Scope        currentScope;

    public TypeCheckVisitor(ScopeVisitor scopeVisitor) {
        this.globalScope = scopeVisitor.getGlobalScope();
        this.currentScope = globalScope;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public Type visitProgram(ProgramContext ctx) {
        for (var func : ctx.functionDeclaration()) {
            var fnName = func.IDENTIFIER().getText();
            var parameters = new ArrayList<ParameterType>();

            if (func.parameterList() != null) {
                for (var param : func.parameterList().parameter()) {
                    var paramName = param.IDENTIFIER().getText();
                    var paramType = param.TYPE().getText();
                    parameters.add(new ParameterType(paramName, getTypeFromString(paramType)));
                }
            }

            FunctionType functionType = new FunctionType(fnName, parameters, Type.errorType());
            functionTypes.put(fnName, functionType);
        }

        for (var func : ctx.functionDeclaration()) {
            visit(func);
        }

        return Type.errorType();
    }

    private Type getTypeFromString(String typeName) {
        return switch (typeName) {
            case "int" -> Type.intType();
            case "bool" -> Type.boolType();
            case "float" -> Type.floatType();
            default -> Type.errorType();
        };
    }

    @Override
    public Type visitLetStatement(LetStatementContext ctx) {
        var name = ctx.identifierExpression().getText();
        var typeName = ctx.TYPE().getText();
        var declaredType = getTypeFromString(typeName);

        variableTypes.put(name, declaredType);

        if (ctx.expression() != null) {
            var initType = visit(ctx.expression());
            if (!initType.equals(declaredType) && !initType.isError()) {
                addError("Type mismatch in variable initialization: expected " + declaredType +
                    ", got " + initType);
            }
        }

        return Type.errorType();
    }

    private void addError(String message) {
        errors.add(message);
    }

    @Override
    public Type visitAssignment(AssignmentContext ctx) {
        var name = ctx.identifierExpression().getText();
        var varType = getVariableType(name);

        if (varType.isError()) {
            addError("Undefined variable: " + name);
            return Type.errorType();
        }

        var exprType = visit(ctx.expression());
        if (!exprType.equals(varType) && !exprType.isError()) {
            addError("Type mismatch in assignment: expected " + varType + ", got " + exprType);
        }

        return Type.errorType();
    }

    private Type getVariableType(String name) {
        var scope = currentScope;
        while (scope != null) {
            if (scope.getVariables().containsKey(name)) {
                var symbol = scope.getVariables().get(name);
                return getTypeFromString(symbol.getType());
            }

            scope = scope.getParent();
        }

        return Type.errorType();
    }

    @Override
    public Type visitIfStatement(IfStatementContext ctx) {
        for (var expr : ctx.expression()) {
            var condType = visit(expr);
            if (!condType.equals(Type.boolType()) && !condType.isError()) {
                addError("Condition must be of type bool, got " + condType);
            }
        }

        for (var stmt : ctx.statementIf()) {
            visit(stmt);
        }

        return Type.errorType();
    }

    @Override
    public Type visitWhileStatement(WhileStatementContext ctx) {
        var condType = visit(ctx.expression());
        if (!condType.equals(Type.boolType()) && !condType.isError()) {
            addError("Condition must be of type bool, got " + condType);
        }

        var previousScope = currentScope;
        for (var childScope : currentScope.getChildren()) {
            if (childScope.getName().startsWith("while")) {
                currentScope = childScope;
                break;
            }
        }

        for (var stmt : ctx.statement()) {
            visit(stmt);
        }

        currentScope = previousScope;

        return Type.errorType();
    }

    @Override
    public Type visitLoopStatement(LoopStatementContext ctx) {
        var previousScope = currentScope;
        for (var childScope : currentScope.getChildren()) {
            if (childScope.getName().startsWith("loop")) {
                currentScope = childScope;
                break;
            }
        }

        for (var stmt : ctx.statement()) {
            visit(stmt);
        }

        currentScope = previousScope;

        return Type.errorType();
    }

    @Override
    public Type visitPrintStatement(PrintStatementContext ctx) {
        visit(ctx.expression());
        return Type.errorType();
    }

    @Override
    public Type visitExpressionStatement(ExpressionStatementContext ctx) {
        visit(ctx.expression());
        return Type.errorType();
    }

    @Override
    public Type visitFunctionDeclaration(FunctionDeclarationContext ctx) {
        String fnName = ctx.IDENTIFIER().getText();
        FunctionType functionType = functionTypes.get(fnName);

        for (ScopeVisitor.Scope childScope : globalScope.getChildren()) {
            if (childScope.getName().equals("fn " + fnName)) {
                currentScope = childScope;
                break;
            }
        }

        visit(ctx.block());
        currentScope = globalScope;

        return Type.functionType(fnName, functionType);
    }

    @Override
    public Type visitBlock(BlockContext ctx) {
        var previousScope = currentScope;
        for (var childScope : currentScope.getChildren()) {
            if (childScope.getName().startsWith("block")) {
                currentScope = childScope;
                break;
            }
        }

        for (var stmt : ctx.statement()) {
            visit(stmt);
        }

        currentScope = previousScope;

        return Type.errorType();
    }

    @Override
    public Type visitExpression(ExpressionContext ctx) {
        return visit(ctx.logicalExpression());
    }

    @Override
    public Type visitLogicalExpression(LogicalExpressionContext ctx) {
        var terms = ctx.comparisonExpression();
        if (terms.size() == 1) {
            return visit(terms.get(0));
        }

        for (var term : terms) {
            var termType = visit(term);
            if (!termType.equals(Type.boolType()) && !termType.isError()) {
                addError("Logical operators require boolean operands, got " + termType);
            }
        }

        return Type.boolType();
    }

    @Override
    public Type visitComparisonExpression(ComparisonExpressionContext ctx) {
        var leftType = visit(ctx.arithmeticExpression(0));
        if (ctx.COMPARISON_OP() == null) {
            return leftType;
        }

        var rightType = visit(ctx.arithmeticExpression(1));
        if (!leftType.equals(rightType) && !leftType.isError() && !rightType.isError()) {
            addError("Comparison operators require matching types, got " + leftType + " and " +
                rightType);
        }

        return Type.boolType();
    }

    @Override
    public Type visitArithmeticExpression(ArithmeticExpressionContext ctx) {
        var leftType = visit(ctx.term(0));
        if (ctx.term().size() == 1) {
            return leftType;
        }

        for (int i = 1; i < ctx.term().size(); i++) {
            var termType = visit(ctx.term(i));
            if (!termType.equals(leftType) && !leftType.isError() && !termType.isError()) {
                addError("Arithmetic operators require matching types, got " + leftType + " and " +
                    termType);
            }

            if (!termType.equals(Type.intType()) && !termType.equals(Type.floatType()) &&
                !termType.isError()) {
                addError("Arithmetic operators require numeric types, got " + termType);
            }
        }

        return leftType;
    }

    @Override
    public Type visitTerm(TermContext ctx) {
        var leftType = visit(ctx.factor(0));
        if (ctx.factor().size() == 1) {
            return leftType;
        }

        for (int i = 1; i < ctx.factor().size(); i++) {
            var factorType = visit(ctx.factor(i));
            if (!factorType.equals(leftType) && !leftType.isError() && !factorType.isError()) {
                addError("Arithmetic operators require matching types, got " + leftType + " and " +
                    factorType);
            }

            if (!factorType.equals(Type.intType()) && !factorType.equals(Type.floatType()) &&
                !factorType.isError()) {
                addError("Arithmetic operators require numeric types, got " + factorType);
            }
        }

        return leftType;
    }

    @Override
    public Type visitFactor(FactorContext ctx) {
        if (ctx.literalExpression() != null) {
            return visit(ctx.literalExpression());
        }

        if (ctx.identifierExpression() != null) {
            var name = ctx.identifierExpression().getText();
            var type = getVariableType(name);
            if (type.isError()) {
                // Check if the identifier is a function name
                if (functionTypes.containsKey(name)) {
                    addError("Function '" + name + "' used as a variable");
                } else {
                    addError("Undefined variable: " + name);
                }
            }

            return type;
        }

        if (ctx.functionCall() != null) {
            return visit(ctx.functionCall());
        }

        if (ctx.expression() != null) {
            return visit(ctx.expression());
        }

        return Type.errorType();
    }

    @Override
    public Type visitLiteralExpression(LiteralExpressionContext ctx) {
        if (ctx.INTEGER_LITERAL() != null) {
            return Type.intType();
        }

        if (ctx.FLOAT_LITERAL() != null) {
            return Type.floatType();
        }

        if (ctx.BOOLEAN_LITERAL() != null) {
            return Type.boolType();
        }

        return Type.errorType();
    }

    @Override
    public Type visitIdentifierExpression(IdentifierExpressionContext ctx) {
        var name = ctx.IDENTIFIER().getText();
        var type = getVariableType(name);

        if (type.isError()) {
            // Check if the identifier is a function name
            if (functionTypes.containsKey(name)) {
                addError("Function '" + name + "' used as a variable");
            } else {
                addError("Undefined variable: " + name);
            }
        }

        return type;
    }

    @Override
    public Type visitFunctionCall(FunctionCallContext ctx) {
        var fnName = ctx.IDENTIFIER().getText();
        var functionType = functionTypes.get(fnName);

        if (functionType == null) {
            addError("Undefined function: " + fnName);
            return Type.errorType();
        }

        List<Type> argumentTypes = new ArrayList<>();
        if (ctx.argumentList() != null) {
            for (var expr : ctx.argumentList().expression()) {
                argumentTypes.add(visit(expr));
            }
        }

        if (!functionType.checkArgumentTypes(argumentTypes)) {
            addError("Function call with wrong argument types: " + fnName);
        }

        return functionType.getReturnType();
    }
}

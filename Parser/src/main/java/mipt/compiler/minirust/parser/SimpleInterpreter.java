package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleInterpreter extends MiniRustBaseVisitor<Object>
{
    public static final double EPSILON = 0.00001;

    static class Variable
    {
        boolean isMutable;
        Object value;

        Variable(boolean isMutable, Object value)
        {
            this.isMutable = isMutable;
            this.value = value;
        }
    }

    Map<String, Variable> variables = new HashMap<>();
    Map<String, MiniRustParser.FunctionDeclarationContext> functions = new HashMap<>();

    @Override
    public Object visitProgram(MiniRustParser.ProgramContext ctx)
    {
        for (MiniRustParser.FunctionDeclarationContext func : ctx.functionDeclaration())
        {
            visit(func);
        }

        if (!functions.containsKey("main"))
        {
            throw new RuntimeException("No 'main' function found.");
        }

        MiniRustParser.FunctionDeclarationContext mainFunc = functions.get("main");
        Map<String, Variable> oldScope = new HashMap<>(variables);
        variables.clear();

        Object result = visit(mainFunc.block());

        variables = oldScope;

        return result;
    }

    @Override
    public Object visitLetStatement(MiniRustParser.LetStatementContext ctx)
    {
        String name = ctx.identifierExpression().getText();
        boolean isMutable = ctx.getChild(1).getText().equals("mut");
        String typeText = ctx.TYPE().getText();

        if (variables.containsKey(name))
        {
            throw new RuntimeException("Duplicate variable: " + name);
        }

        Object value;
        if (ctx.expression() != null)
        {
            value = visit(ctx.expression());
        }
        else
        {
            value = defaultValueForType(typeText);
        }

        Variable var = new Variable(isMutable, value);
        variables.put(name, var);

        // System.out.println("Let statement: " + name + " " + value);

        return null;
    }

    private Object defaultValueForType(String type)
    {
        return switch (type)
        {
            case "int" -> 0;
            case "float" -> 0.0;
            case "bool" -> false;
            default -> throw new RuntimeException("Unsupported type: " + type);
        };
    }

    @Override
    public Object visitAssignment(MiniRustParser.AssignmentContext ctx)
    {
        String name = ctx.identifierExpression().getText();
        if (!variables.containsKey(name))
        {
            throw new RuntimeException("Unknown variable: " + name);
        }

        Variable var = variables.get(name);
        if (!var.isMutable)
        {
            throw new RuntimeException("Cannot assign to immutable variable: " + name);
        }

        var.value = visit(ctx.expression());

        return null;
    }

    @Override
    public Object visitIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx)
    {
        String name = ctx.IDENTIFIER().getText();
        if (!variables.containsKey(name))
        {
            throw new RuntimeException("Unknown variable: " + name);
        }

        Object value = variables.get(name).value;
        if (value == null)
        {
            throw new RuntimeException("Variable '" + name + "' used before initialization.");
        }

        return value;
    }

    @Override
    public Object visitFunctionDeclaration(MiniRustParser.FunctionDeclarationContext ctx)
    {
        String name = ctx.IDENTIFIER().getText();
        if (functions.containsKey(name))
            throw new RuntimeException("Duplicate function: " + name);

        functions.put(name, ctx);
        return null;
    }

    @Override
    public Object visitFunctionCall(MiniRustParser.FunctionCallContext ctx)
    {
        String name = ctx.IDENTIFIER().getText();
        if (!functions.containsKey(name))
            throw new RuntimeException("Unknown function: " + name);

        MiniRustParser.FunctionDeclarationContext func = functions.get(name);

        List<MiniRustParser.ParameterContext> params = func.parameterList() != null
                ? func.parameterList().parameter()
                : List.of();

        List<MiniRustParser.ExpressionContext> args = ctx.argumentList() != null
                ? ctx.argumentList().expression()
                : List.of();

        if (params.size() != args.size())
            throw new RuntimeException("Function argument count mismatch");

        Map<String, Variable> oldScope = this.variables;
        Map<String, Variable> localScope = new HashMap<>();

        for (int i = 0; i < params.size(); i++)
        {
            String paramName = params.get(i).IDENTIFIER().getText();
            String paramType = params.get(i).TYPE().getText();

            Object argValue = visit(args.get(i));
            Object defaultValue = argValue != null ? argValue : defaultValueForType(paramType);

            localScope.put(paramName, new Variable(true, defaultValue));
        }

        this.variables = localScope;
        Object result = visit(func.block());
        this.variables = oldScope;

        return result;
    }

    @Override
    public Object visitLiteralExpression(MiniRustParser.LiteralExpressionContext ctx)
    {
        if (ctx.INTEGER_LITERAL() != null)
        {
            return Integer.parseInt(ctx.INTEGER_LITERAL().getText());
        }
        if (ctx.FLOAT_LITERAL() != null)
        {
            return Double.parseDouble(ctx.FLOAT_LITERAL().getText());
        }
        if (ctx.BOOLEAN_LITERAL() != null)
        {
            return Boolean.parseBoolean(ctx.BOOLEAN_LITERAL().getText());
        }
        throw new RuntimeException("Unknown literal: " + ctx.getText());
    }

    @Override
    public Object visitPrintStatement(MiniRustParser.PrintStatementContext ctx)
    {
        Object value = visit(ctx.expression());
        System.out.println("OUTPUT : " + value);
        return null;
    }

    @Override
    public Object visitArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx)
    {
        Object result = visit(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++)
        {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.term(i));

            if (result instanceof Integer && right instanceof Integer)
            {
                result = op.equals("+") ? (Integer) result + (Integer) right : (Integer) result - (Integer) right;
            }
            else
            {
                result = op.equals("+")
                        ? toDouble(result) + toDouble(right)
                        : toDouble(result) - toDouble(right);
            }
        }
        return result;
    }

    @Override
    public Object visitTerm(MiniRustParser.TermContext ctx)
    {
        Object result = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++)
        {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.factor(i));

            if (result instanceof Integer && right instanceof Integer)
            {
                result = op.equals("*") ? (Integer) result * (Integer) right : (Integer) result / (Integer) right;
            }
            else
            {
                result = op.equals("*")
                        ? toDouble(result) * toDouble(right)
                        : toDouble(result) / toDouble(right);
            }
        }

        return result;
    }

    @Override
    public Object visitFactor(MiniRustParser.FactorContext ctx)
    {
        if (ctx.expression() != null)
        {
            return visit(ctx.expression());
        }
        if (ctx.literalExpression() != null)
        {
            return visit(ctx.literalExpression());
        }
        if (ctx.identifierExpression() != null)
        {
            return visit(ctx.identifierExpression());
        }
        if (ctx.functionCall() != null)
        {
            return visit(ctx.functionCall());
        }

        throw new RuntimeException("Unknown factor: " + ctx.getText());
    }

    @Override
    public Object visitComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx)
    {
        Object left = visit(ctx.arithmeticExpression(0));

        if (ctx.arithmeticExpression().size() == 1)
        {
            return left;
        }

        Object right = visit(ctx.arithmeticExpression(1));
        String op = ctx.COMPARISON_OP().getText();

        double l = toDouble(left);
        double r = toDouble(right);

        // System.out.println("left : " + l + " right : " + r);

        return switch (op)
        {
            case "==" -> Math.abs(l - r) < EPSILON;
            case "!=" -> Math.abs(l - r) >= EPSILON;
            case ">" -> l > r;
            case "<" -> l < r;
            case ">=" -> l >= r || Math.abs(l - r) < EPSILON;
            case "<=" -> l <= r || Math.abs(l - r) < EPSILON;
            default -> throw new RuntimeException("Unknown comparison operator: " + op);
        };
    }

    @Override
    public Object visitLogicalExpression(MiniRustParser.LogicalExpressionContext ctx)
    {
        Object left = visit(ctx.comparisonExpression(0));
        for (int i = 1; i < ctx.comparisonExpression().size(); i++)
        {
            String op = ctx.getChild(2 * i - 1).getText();
            Object right = visit(ctx.comparisonExpression(i));

            boolean l = toBoolean(left);
            boolean r = toBoolean(right);

            left = op.equals("&&") ? (l && r) : (l || r);
        }
        return left;
    }

    @Override
    public Object visitIfStatement(MiniRustParser.IfStatementContext ctx)
    {
        int stmtIndex = 0;

        for (int i = 0; i < ctx.expression().size(); i++)
        {
            if (toBoolean(visit(ctx.expression(i))))
            {
                List<MiniRustParser.StatementIfContext> statements = ctx.statementIf();
                int stmtCount = getStatementCountInBranch(ctx, i);
                for (int j = 0; j < stmtCount; j++)
                {
                    visit(statements.get(stmtIndex++));
                }
                return null;
            }
            else
            {
                stmtIndex += getStatementCountInBranch(ctx, i);
            }
        }

        if (ctx.getText().contains("else"))
        {
            int elseCount = getStatementCountInBranch(ctx, ctx.expression().size());
            List<MiniRustParser.StatementIfContext> statements = ctx.statementIf();
            for (int j = 0; j < elseCount; j++)
            {
                visit(statements.get(stmtIndex++));
            }
        }

        return null;
    }

    private int getStatementCountInBranch(MiniRustParser.IfStatementContext ctx, int branchIndex)
    {
        int branchCounter = -1;
        int count = 0;

        for (var child : ctx.children)
        {
            if (child.getText().equals("if") || child.getText().equals("else") || child.getText().equals("else if"))
            {
                branchCounter++;
                continue;
            }

            if (branchCounter == branchIndex && child instanceof MiniRustParser.StatementIfContext)
            {
                count++;
            }
            else if (branchCounter > branchIndex)
            {
                break;
            }
        }

        return count;
    }

    private int countStatementIfInBranch(MiniRustParser.IfStatementContext ctx, int branchIndex)
    {
        int count = 0;
        int level = 0;
        for (var child : ctx.children)
        {
            if (child instanceof MiniRustParser.StatementIfContext)
            {
                count++;
            }
            else if (child.getText().equals("if") || child.getText().equals("else if") || child.getText().equals("else"))
            {
                if (level == branchIndex) return count;
                level++;
                count = 0;
            }
        }
        return count;
    }


    @Override
    public Object visitWhileStatement(MiniRustParser.WhileStatementContext ctx)
    {
        while (toBoolean(visit(ctx.expression())))
        {
            for (var stmt : ctx.statement())
            {
                visit(stmt);
            }
        }
        return null;
    }

    @Override
    public Object visitLoopStatement(MiniRustParser.LoopStatementContext ctx)
    {
        int count = 0;
        while (count++ < 1000)
        {
            for (var stmt : ctx.statement())
            {
                visit(stmt);
            }
        }
        return null;
    }

    @Override
    public Object visitExpressionStatement(MiniRustParser.ExpressionStatementContext ctx)
    {
        return visit(ctx.expression());
    }

    private double toDouble(Object value)
    {
        if (value == null)
        {
            throw new RuntimeException("Cannot convert null to double — possible uninitialized variable or missing return from expression.");
        }

        if (value instanceof Integer) return ((Integer) value).doubleValue();
        if (value instanceof Double) return (Double) value;
        if (value instanceof Boolean) return ((Boolean) value) ? 1.0 : 0.0;

        throw new RuntimeException("Unsupported value type for numeric operation: " + value.getClass());
    }


    private boolean toBoolean(Object value)
    {
        return (value instanceof Boolean && (Boolean) value) ||
                (value instanceof Integer && (Integer) value != 0) ||
                (value instanceof Double && !(Math.abs((Double) value) < EPSILON));
    }
}

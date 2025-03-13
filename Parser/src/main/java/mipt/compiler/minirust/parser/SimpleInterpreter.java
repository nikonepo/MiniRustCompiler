package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser;

import java.util.HashMap;
import java.util.Map;

public class SimpleInterpreter extends MiniRustBaseVisitor<Integer>
{
    Map<String, Integer> variables = new HashMap<>();

    @Override
    public Integer visitLetStatement(MiniRustParser.LetStatementContext ctx)
    {
        String identifier = ctx.identifierExpression().getText();

        if (variables.containsKey(identifier))
        {
            throw new RuntimeException("Duplicate variable found: " + identifier);
        }

        Integer value = visit(ctx.expression());
        variables.put(identifier, value);

        return 0;
    }

    @Override
    public Integer visitAssignment(MiniRustParser.AssignmentContext ctx)
    {
        String identifier = ctx.identifierExpression().getText();
        if (!variables.containsKey(identifier))
        {
            throw new RuntimeException("Unknown identifier: " + identifier);
        }

        int value = visit(ctx.expression());
        variables.put(identifier, value);

        return 0;
    }

    @Override
    public Integer visitArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx)
    {
        int result = visit(ctx.term(0));

        for (int i = 1; i < ctx.term().size(); i++)
        {
            int right = visit(ctx.term(1));

            if (ctx.getChild(1).getText().equals("+"))
            {
                result = result + right;
            }
            else
            {
                result = result - right;
            }
        }

        return result;
    }

    @Override
    public Integer visitTerm(MiniRustParser.TermContext ctx)
    {
        int result = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++)
        {
            int right = visit(ctx.factor(1));

            if (ctx.getChild(1).getText().equals("*"))
            {
                result = result * right;
            }
            else
            {
                result = result / right;
            }
        }

        return result;
    }

    @Override
    public Integer visitFactor(MiniRustParser.FactorContext ctx)
    {
        Integer value = 0;

        if (ctx.literalExpression() != null)
        {
            value = visit(ctx.literalExpression());
        }

        if (ctx.identifierExpression() != null)
        {
            value = visit(ctx.identifierExpression());
        }

        if (ctx.arithmeticExpression() != null)
        {
            value = visit(ctx.arithmeticExpression());
        }

        if (value == null)
        {
            throw new RuntimeException("Unknown factor: " + ctx.getText());
        }

        return value;
    }

    @Override
    public Integer visitLiteralExpression(MiniRustParser.LiteralExpressionContext ctx)
    {
        String intLiteral = ctx.INTEGER_LITERAL().getText();
        return Integer.parseInt(intLiteral);
    }

    @Override
    public Integer visitIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx)
    {
        String identifier = ctx.IDENTIFIER().getText();

        if (!variables.containsKey(identifier))
        {
            throw new RuntimeException("Unknown identifier: " + identifier);
        }

        return variables.get(identifier);
    }

    @Override
    public Integer visitPrintStatement(MiniRustParser.PrintStatementContext ctx)
    {
        System.out.println("---- MiniRust print: " + visit(ctx.expression()));
        return 0;
    }

    @Override
    public Integer visitComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx)
    {
        int left = visit(ctx.arithmeticExpression(0));
        int right = visit(ctx.arithmeticExpression(1));

        String op = ctx.getChild(1).getText();

        boolean result = switch (op)
        {
            case "<=" -> left <= right;
            case "<" -> left < right;
            case ">=" -> left >= right;
            case ">" -> left > right;
            case "==" -> left == right;
            case "!=" -> left != right;
            default -> throw new RuntimeException("Unknown comparison operator: " + op);
        };

        return result ? 1 : 0;
    }

    @Override
    public Integer visitIfStatement(MiniRustParser.IfStatementContext ctx)
    {
        int result = visit(ctx.comparisonExpression());

        if (result == 1)
        {
            ctx.statementIf().forEach(this::visit);
        }

        return 0;
    }
}

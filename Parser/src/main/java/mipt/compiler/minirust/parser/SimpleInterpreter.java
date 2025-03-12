package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser;

import java.util.HashMap;
import java.util.Map;

public class SimpleInterpreter extends MiniRustBaseVisitor<Integer> {
    Map<String, Integer> variables = new HashMap<>();

    @Override
    public Integer visitLetStatement(MiniRustParser.LetStatementContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx) {
        int result = visit(ctx.term(0));

        for (int i = 1; i < ctx.term().size(); i++) {
            int right = visit(ctx.term(1));

            if (ctx.getChild(1).getText().equals("+")) {
                result = result + right;
            } else {
                result = result - right;
            }
        }

        return result;
    }

    @Override
    public Integer visitTerm(MiniRustParser.TermContext ctx) {
        int result = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++) {
            int right = visit(ctx.factor(1));

            if (ctx.getChild(1).getText().equals("*")) {
                result = result * right;
            } else {
                result = result / right;
            }
        }

        return result;
    }

    @Override
    public Integer visitFactor(MiniRustParser.FactorContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Integer visitPrintStatement(MiniRustParser.PrintStatementContext ctx) {
        System.out.println("---- MiniRust print: " + visitChildren(ctx));
        return 0;
    }

    @Override
    public Integer visitLiteralexpression(MiniRustParser.LiteralexpressionContext ctx) {
        String intLiteral = ctx.INTEGER_LITERAL().getText();

        return Integer.parseInt(intLiteral);
    }
}

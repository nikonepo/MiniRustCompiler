package mipt.compiler.minirust.ir;

import mipt.compiler.minirust.ir.internal.*;
import mipt.compiler.minirust.ir.internal.IROperation.Op;
import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IRTranslateVisitor extends MiniRustBaseVisitor<String>
{
    private final IRModule module = new IRModule();
    private IRFunction currentFunction;
    private final Map<String, String> variableMap = new HashMap<>();
    private int tempCounter = 0;
    private int labelCounter = 0;
    private int paramCounter = 0;

    public IRModule getModule()
    {
        return module;
    }

    private String newTemp()
    {
        return "%t" + tempCounter++;
    }

    private String newLabel(String prefix)
    {
        return prefix + labelCounter++;
    }

    private String newParam()
    {
        return "%p" + paramCounter++;
    }

    @Override
    public String visitProgram(MiniRustParser.ProgramContext ctx)
    {
        for (var func : ctx.functionDeclaration())
        {
            visit(func);
        }
        return module.toString();
    }

    @Override
    public String visitFunctionDeclaration(MiniRustParser.FunctionDeclarationContext ctx)
    {
        paramCounter = 0;
        String name = ctx.IDENTIFIER().getText();
        currentFunction = new IRFunction(name);
        module.addFunction(currentFunction);

        if (ctx.parameterList() != null)
        {
            for (var param : ctx.parameterList().parameter())
            {
                String paramName = param.IDENTIFIER().getText();
                String paramAddr = newParam();
                variableMap.put(paramName, paramAddr);
            }
        }

        visit(ctx.block());
        return null;
    }

    @Override
    public String visitLetStatement(MiniRustParser.LetStatementContext ctx)
    {
        String varName = ctx.identifierExpression().getText();
        String type = ctx.TYPE().getText();
        String varAddr = newTemp();
        variableMap.put(varName, varAddr);
        currentFunction.add(new IRCreate(varAddr, type));

        if (ctx.expression() != null)
        {
            String value = visitExpr(ctx.expression());
            currentFunction.add(new IRStore(value, varAddr));
        }
        return null;
    }

    @Override
    public String visitAssignment(MiniRustParser.AssignmentContext ctx)
    {
        String varName = ctx.identifierExpression().getText();
        String value = visitExpr(ctx.expression());
        String addr = variableMap.get(varName);
        currentFunction.add(new IRStore(value, addr));
        return null;
    }

    @Override
    public String visitExpressionStatement(MiniRustParser.ExpressionStatementContext ctx)
    {
        visitExpr(ctx.expression());
        return null;
    }

    @Override
    public String visitPrintStatement(MiniRustParser.PrintStatementContext ctx)
    {
        String value = visitExpr(ctx.expression());
        currentFunction.add(new IRCall(null, "print", List.of(value)));
        return null;
    }

    @Override
    public String visitIfStatement(MiniRustParser.IfStatementContext ctx) {
        int branchCount = ctx.expression().size();
        boolean hasElse = ctx.getText().contains("else");

        List<String> thenLabels = new ArrayList<>(branchCount);
        for (int i = 0; i < branchCount; i++) {
            thenLabels.add(newLabel("then"));
        }

        String elseLabel = hasElse ? newLabel("else") : null;
        String endLabel = newLabel("endif");

        for (int i = 0; i < branchCount; i++) {
            String cond = visitExpr(ctx.expression(i));

            String falseTarget;
            if (i + 1 < branchCount) {
                falseTarget = thenLabels.get(i + 1);
            } else if (hasElse) {
                falseTarget = elseLabel;
            } else {
                falseTarget = endLabel;
            }

            currentFunction.add(new IRCondJump(cond,
                thenLabels.get(i),
                falseTarget));
        }

        if (hasElse) {
            currentFunction.add(new IRLabel(elseLabel));
            for (int i = branchCount; i < ctx.statementIf().size(); i++) {
                visit(ctx.statementIf(i));
            }
            currentFunction.add(new IRJump(endLabel));
        }

        int stmtIdx = 0;
        for (int i = 0; i < branchCount; i++) {
            currentFunction.add(new IRLabel(thenLabels.get(i)));

            int count = getStatementCountInBranch(ctx, i);
            for (int j = 0; j < count; j++) {
                visit(ctx.statementIf(stmtIdx++));
            }

            currentFunction.add(new IRJump(endLabel));
        }

        currentFunction.add(new IRLabel(endLabel));

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

    @Override
    public String visitWhileStatement(MiniRustParser.WhileStatementContext ctx)
    {
        String condLabel = newLabel("cond");
        String bodyLabel = newLabel("body");
        String endLabel = newLabel("end");

        currentFunction.add(new IRLabel(condLabel));
        String cond = visitExpr(ctx.expression());

        currentFunction.add(new IRCondJump(cond, bodyLabel, endLabel));

        currentFunction.add(new IRLabel(bodyLabel));
        ctx.statement().forEach(this::visit);
        currentFunction.add(new IRJump(condLabel));

        currentFunction.add(new IRLabel(endLabel));
        return null;
    }

    @Override
    public String visitLoopStatement(MiniRustParser.LoopStatementContext ctx)
    {
        String loopLabel = newLabel("loop");
        currentFunction.add(new IRLabel(loopLabel));
        ctx.statement().forEach(this::visit);
        currentFunction.add(new IRJump(loopLabel));
        return null;
    }

    private String visitExpr(MiniRustParser.ExpressionContext ctx)
    {
        return visitLogical(ctx.logicalExpression());
    }

    private String visitLogical(MiniRustParser.LogicalExpressionContext ctx)
    {
        List<MiniRustParser.ComparisonExpressionContext> terms = ctx.comparisonExpression();
        if (terms.size() == 1) return visitComparison(terms.getFirst());
        String acc = visitComparison(terms.getFirst());
        for (int i = 1; i < terms.size(); i++)
        {
            String rhs = visitComparison(terms.get(i));
            String res = newTemp();
            currentFunction.add(new IROperation(res, acc, rhs, Op.OR));
            acc = res;
        }
        return acc;
    }

    private String visitComparison(MiniRustParser.ComparisonExpressionContext ctx)
    {
        String lhs = visitArithmetic(ctx.arithmeticExpression(0));
        if (ctx.COMPARISON_OP() == null) return lhs;
        String rhs = visitArithmetic(ctx.arithmeticExpression(1));
        String res = newTemp();
        String opText = ctx.COMPARISON_OP().getText();

        Op op = switch (opText)
        {
            case "==" -> Op.EQ;
            case "!=" -> Op.NE;
            case "<" -> Op.LT;
            case "<=" -> Op.LE;
            case ">" -> Op.GT;
            case ">=" -> Op.GE;
            default -> throw new RuntimeException("Unknown comparison op: " + opText);
        };
        currentFunction.add(new IROperation(res, lhs, rhs, op));
        return res;
    }

    private String visitArithmetic(MiniRustParser.ArithmeticExpressionContext ctx)
    {
        String acc = visitTerm(ctx.term(0));
        for (int i = 1; i < ctx.term().size(); i++)
        {
            String rhs = visitTerm(ctx.term(i));
            String opText = ctx.getChild(2 * i - 1).getText();
            String res = newTemp();
            Op op = switch (opText)
            {
                case "+" -> Op.ADD;
                case "-" -> Op.SUB;
                default -> throw new RuntimeException("Unknown arithmetic op: " + opText);
            };
            currentFunction.add(new IROperation(res, acc, rhs, op));
            acc = res;
        }
        return acc;
    }

    public String visitTerm(MiniRustParser.TermContext ctx)
    {
        String acc = visitFactor(ctx.factor(0));
        for (int i = 1; i < ctx.factor().size(); i++)
        {
            String rhs = visitFactor(ctx.factor(i));
            String opText = ctx.getChild(2 * i - 1).getText();
            String res = newTemp();
            Op op = switch (opText)
            {
                case "*" -> Op.MUL;
                case "/" -> Op.DIV;
                default -> throw new RuntimeException("Unknown term op: " + opText);
            };
            currentFunction.add(new IROperation(res, acc, rhs, op));
            acc = res;
        }
        return acc;
    }

    public String visitFactor(MiniRustParser.FactorContext ctx)
    {
        if (ctx.literalExpression() != null)
        {
            return ctx.literalExpression().getText();
        }
        if (ctx.identifierExpression() != null)
        {
            String var = ctx.identifierExpression().getText();
            String addr = variableMap.get(var);
            String temp = newTemp();
            currentFunction.add(new IRLoad(temp, addr));
            return temp;
        }
        if (ctx.functionCall() != null)
        {
            List<String> args = new ArrayList<>();
            if (ctx.functionCall().argumentList() != null)
            {
                for (var expr : ctx.functionCall().argumentList().expression())
                {
                    String argValue = visitExpr(expr);
                    args.add(argValue);
                }
            }
            String fnName = ctx.functionCall().IDENTIFIER().getText();
            if (fnName.equals("print") && args.size() == 1)
            {
                currentFunction.add(new IRCall(null, "print", args));
                return null;
            }
            else
            {
                for (int i = 0; i < args.size(); i++)
                {
                    currentFunction.add(new IRStore(args.get(i), "%p" + i));
                }
                String res = newTemp();
                currentFunction.add(new IRCall(res, fnName, List.of()));
                return res;
            }
        }
        if (ctx.expression() != null)
        {
            return visitExpr(ctx.expression());
        }
        throw new RuntimeException("Unknown factor: " + ctx.getText());
    }
}

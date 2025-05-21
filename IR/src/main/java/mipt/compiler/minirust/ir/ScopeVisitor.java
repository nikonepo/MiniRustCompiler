package mipt.compiler.minirust.ir;

import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import mipt.compiler.minirust.parser.internal.MiniRustParser;

import java.util.*;

public class ScopeVisitor extends MiniRustBaseVisitor<Void>
{
    public static class Scope
    {
        Scope parent;
        List<Scope> children = new ArrayList<>();
        Map<String, VariableSymbol> variables = new LinkedHashMap<>();
        Map<String, String> statuses = new HashMap<>();
        Map<String, String> shadows = new HashMap<>();
        Set<String> usedVariables = new HashSet<>();
        String name;

        public Scope(Scope parent, String name)
        {
            this.parent = parent;
            this.name = name;
            if (parent != null) parent.children.add(this);
        }

        boolean declareVariable(String name, String type, boolean isMut)
        {
            if (variables.containsKey(name))
            {
                statuses.put(name, "duplicate");
                return false;
            }
            else
            {
                if (resolveInParent(name))
                {
                    statuses.put(name, "shadow");
                    shadows.put(name, parent.getFullScopePathFor(name));
                }
                else
                {
                    statuses.put(name, "ok");
                }
                variables.put(name, new VariableSymbol(name, type, isMut));
                return true;
            }
        }

        boolean resolveVariable(String name)
        {
            if (variables.containsKey(name)) return true;
            if (parent != null) return parent.resolveVariable(name);
            return false;
        }

        boolean resolveInParent(String name)
        {
            if (parent == null) return false;
            if (parent.variables.containsKey(name)) return true;
            return parent.resolveInParent(name);
        }

        String getFullScopePathFor(String varName)
        {
            if (variables.containsKey(varName)) return name;
            if (parent != null) return parent.getFullScopePathFor(varName);
            return "?";
        }
    }

    private final Scope globalScope = new Scope(null, "global");
    private Scope currentScope = globalScope;
    private int scopeCounter = 0;
    private final Map<String, FunctionSymbol> functions = new LinkedHashMap<>();

    public String generateDot()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ScopeTree {\n");
        sb.append("  node [shape=box, fontname=\"Courier\"];\n");
        printScope(sb, globalScope);
        sb.append("}\n");
        return sb.toString();
    }

    private void printScope(StringBuilder sb, Scope scope)
    {
        StringBuilder label = new StringBuilder();
        label.append(scope.name).append("\\n----------------\\n");
        for (var entry : scope.variables.entrySet())
        {
            String varName = entry.getKey();
            VariableSymbol var = entry.getValue();
            String status = scope.statuses.getOrDefault(varName, "ok");

            if (status.equals("ok") && !scope.usedVariables.contains(varName)) {
                status = "unused";
            }

            label.append(var.getName()).append(" : ")
                    .append(var.getType())
                    .append(var.isMutable() ? " (mut)" : " (immut)")
                    .append(" [").append(status).append("]\\n");

            if (status.equals("shadow"))
            {
                String shadowFrom = scope.shadows.get(varName);
                String fromId = shadowFrom.replaceAll("[^a-zA-Z0-9]", "_");
                String thisId = scope.name.replaceAll("[^a-zA-Z0-9]", "_");
                sb.append("  ").append(thisId).append(" -> ").append(fromId)
                        .append(" [label=\"shadows ").append(varName).append("\"]\n");
            }
        }

        String nodeId = scope.name.replaceAll("[^a-zA-Z0-9]", "_");
        sb.append("  ").append(nodeId).append(" [label=\"").append(label).append("\"]\n");

        for (Scope child : scope.children)
        {
            String childId = child.name.replaceAll("[^a-zA-Z0-9]", "_");
            sb.append("  ").append(nodeId).append(" -> ").append(childId).append("\n");
            printScope(sb, child);
        }
    }

    @Override
    public Void visitIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx) {
        String name = ctx.IDENTIFIER().getText();
        Scope scope = currentScope;
        while (scope != null) {
            if (scope.variables.containsKey(name)) {
                scope.usedVariables.add(name);
                break;
            }
            scope = scope.parent;
        }
        return null;
    }

    @Override
    public Void visitFunctionDeclaration(MiniRustParser.FunctionDeclarationContext ctx)
    {
        String fnName = ctx.IDENTIFIER().getText();
        Scope fnScope = new Scope(globalScope, "fn " + fnName);
        currentScope = fnScope;

        List<String> paramNames = new ArrayList<>();
        if (ctx.parameterList() != null)
        {
            for (var param : ctx.parameterList().parameter())
            {
                String name = param.IDENTIFIER().getText();
                String type = param.TYPE().getText();
                currentScope.declareVariable(name, type, false);
                paramNames.add(name);
            }
        }

        functions.put(fnName, new FunctionSymbol(fnName, paramNames));
        visit(ctx.block());
        currentScope = globalScope;
        return null;
    }

    @Override
    public Void visitBlock(MiniRustParser.BlockContext ctx)
    {
        Scope newScope = new Scope(currentScope, "block" + scopeCounter++);
        Scope previous = currentScope;
        currentScope = newScope;

        for (var stmt : ctx.statement()) visit(stmt);

        currentScope = previous;
        return null;
    }

    @Override
    public Void visitLetStatement(MiniRustParser.LetStatementContext ctx)
    {
        String name = ctx.identifierExpression().getText();
        String type = ctx.TYPE().getText();
        boolean isMut = ctx.getChild(1).getText().equals("mut");
        currentScope.declareVariable(name, type, isMut);
        if (ctx.expression() != null) visit(ctx.expression());
        return null;
    }

    @Override
    public Void visitAssignment(MiniRustParser.AssignmentContext ctx)
    {
        String name = ctx.identifierExpression().getText();
        if (!currentScope.resolveVariable(name))
        {
            currentScope.variables.put(name, new VariableSymbol(name, "???", true));
            currentScope.statuses.put(name, "undeclared");
        }
        visit(ctx.expression());
        return null;
    }

    @Override
    public Void visitExpressionStatement(MiniRustParser.ExpressionStatementContext ctx)
    {
        visit(ctx.expression());
        return null;
    }

    @Override
    public Void visitPrintStatement(MiniRustParser.PrintStatementContext ctx)
    {
        visit(ctx.expression());
        return null;
    }

    @Override
    public Void visitIfStatement(MiniRustParser.IfStatementContext ctx)
    {
        for (var expr : ctx.expression()) visit(expr);
        for (var stmt : ctx.statementIf()) visit(stmt);
        return null;
    }

    @Override
    public Void visitWhileStatement(MiniRustParser.WhileStatementContext ctx)
    {
        visit(ctx.expression());
        Scope whileScope = new Scope(currentScope, "while" + scopeCounter++);
        Scope previous = currentScope;
        currentScope = whileScope;
        for (var stmt : ctx.statement()) visit(stmt);
        currentScope = previous;
        return null;
    }

    @Override
    public Void visitLoopStatement(MiniRustParser.LoopStatementContext ctx)
    {
        Scope loopScope = new Scope(currentScope, "loop" + scopeCounter++);
        Scope previous = currentScope;
        currentScope = loopScope;
        for (var stmt : ctx.statement()) visit(stmt);
        currentScope = previous;
        return null;
    }
}

class VariableSymbol
{
    private final String name;
    private final String type;
    private final boolean mutable;

    public VariableSymbol(String name, String type, boolean mutable)
    {
        this.name = name;
        this.type = type;
        this.mutable = mutable;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public boolean isMutable()
    {
        return mutable;
    }
}

class FunctionSymbol
{
    private final String name;
    private final List<String> parameters;

    public FunctionSymbol(String name, List<String> parameters)
    {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getParameters()
    {
        return parameters;
    }
}

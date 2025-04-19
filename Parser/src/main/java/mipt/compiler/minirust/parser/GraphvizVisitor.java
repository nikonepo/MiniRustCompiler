package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.internal.MiniRustBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.concurrent.atomic.AtomicInteger;

public class GraphvizVisitor extends MiniRustBaseVisitor<Void>
{
    private final StringBuilder builder = new StringBuilder();
    private final AtomicInteger nodeCounter = new AtomicInteger();
    private final StringBuilder nodes = new StringBuilder();
    private final StringBuilder edges = new StringBuilder();

    public String generateDot(ParseTree tree)
    {
        builder.setLength(0);
        nodes.setLength(0);
        edges.setLength(0);

        builder.append("digraph ParseTree {\n");
        builder.append("node [shape=box, style=filled, fillcolor=lightgrey];\n");
        visit(tree, -1); // Root has no parent
        builder.append(nodes).append(edges);
        builder.append("}");
        return builder.toString();
    }

    private int visit(ParseTree tree, int parentId)
    {
        int currentId = nodeCounter.getAndIncrement();

        String label = escape(getNodeLabel(tree));
        nodes.append(String.format("  node%d [label=\"%s\"];\n", currentId, label));

        if (parentId >= 0)
        {
            edges.append(String.format("  node%d -> node%d;\n", parentId, currentId));
        }

        for (int i = 0; i < tree.getChildCount(); i++)
        {
            visit(tree.getChild(i), currentId);
        }

        return currentId;
    }

    private String getNodeLabel(ParseTree tree)
    {
        if (tree instanceof RuleNode)
        {
            return tree.getClass().getSimpleName().replace("Context", "");
        }
        else
        {
            return tree.getText();
        }
    }

    private String escape(String label)
    {
        return label.replace("\"", "\\\"");
    }
}

// Generated from C:/Users/anton/Desktop/MIPT/compilerDevelopment/Parser/src/main/java/mipt/compiler/minirust/parser/MiniRust.g4 by ANTLR 4.13.2
package mipt.compiler.minirust.parser.internal;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MiniRustParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MiniRustVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(MiniRustParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(MiniRustParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#statementIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementIf(MiniRustParser.StatementIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#letStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetStatement(MiniRustParser.LetStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#assignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment(MiniRustParser.AssignmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(MiniRustParser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(MiniRustParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MiniRustParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#literalexpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralexpression(MiniRustParser.LiteralexpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#operatorExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorExpression(MiniRustParser.OperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(MiniRustParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(MiniRustParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx);
}
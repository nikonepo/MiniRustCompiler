// Generated from D:/GitReps/MiniRustCompiler/Parser/src/main/java/mipt/compiler/minirust/parser/rules/MiniRust.g4 by ANTLR 4.13.2
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
	 * Visit a parse tree produced by {@link MiniRustParser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(MiniRustParser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#loopStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLoopStatement(MiniRustParser.LoopStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#printStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStatement(MiniRustParser.PrintStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(MiniRustParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#functionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDeclaration(MiniRustParser.FunctionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#parameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterList(MiniRustParser.ParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(MiniRustParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(MiniRustParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(MiniRustParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#logicalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalExpression(MiniRustParser.LogicalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#comparisonExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx);
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
	 * Visit a parse tree produced by {@link MiniRustParser#literalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(MiniRustParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#identifierExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(MiniRustParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiniRustParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(MiniRustParser.ArgumentListContext ctx);
}
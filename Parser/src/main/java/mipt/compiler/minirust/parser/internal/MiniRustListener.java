// Generated from D:/GitReps/compiler_course/Parser/src/main/java/mipt/compiler/minirust/parser/rules/MiniRust.g4 by ANTLR 4.13.2
package mipt.compiler.minirust.parser.internal;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniRustParser}.
 */
public interface MiniRustListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MiniRustParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MiniRustParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(MiniRustParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(MiniRustParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#statementIf}.
	 * @param ctx the parse tree
	 */
	void enterStatementIf(MiniRustParser.StatementIfContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#statementIf}.
	 * @param ctx the parse tree
	 */
	void exitStatementIf(MiniRustParser.StatementIfContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#letStatement}.
	 * @param ctx the parse tree
	 */
	void enterLetStatement(MiniRustParser.LetStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#letStatement}.
	 * @param ctx the parse tree
	 */
	void exitLetStatement(MiniRustParser.LetStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(MiniRustParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(MiniRustParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MiniRustParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MiniRustParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void enterPrintStatement(MiniRustParser.PrintStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#printStatement}.
	 * @param ctx the parse tree
	 */
	void exitPrintStatement(MiniRustParser.PrintStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(MiniRustParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(MiniRustParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#operatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterOperatorExpression(MiniRustParser.OperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#operatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitOperatorExpression(MiniRustParser.OperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 */
	void enterArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#arithmeticExpression}.
	 * @param ctx the parse tree
	 */
	void exitArithmeticExpression(MiniRustParser.ArithmeticExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(MiniRustParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(MiniRustParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(MiniRustParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(MiniRustParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(MiniRustParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(MiniRustParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#literalExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(MiniRustParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link MiniRustParser#identifierExpression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link MiniRustParser#identifierExpression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(MiniRustParser.IdentifierExpressionContext ctx);
}
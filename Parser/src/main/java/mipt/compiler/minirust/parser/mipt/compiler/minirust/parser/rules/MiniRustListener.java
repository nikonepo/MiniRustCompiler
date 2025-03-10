// Generated from D:/GitReps/compiler_course/Parser/src/main/java/mipt/compiler/minirust/parser/rules/MiniRust.g4 by ANTLR 4.13.2
package mipt.compiler.minirust.parser.rules;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MiniRustParser}.
 */
public interface MiniRustListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code SingleTerm}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSingleTerm(MiniRustParser.SingleTermContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleTerm}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSingleTerm(MiniRustParser.SingleTermContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(MiniRustParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(MiniRustParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(MiniRustParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(MiniRustParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingleFactor}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void enterSingleFactor(MiniRustParser.SingleFactorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleFactor}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 */
	void exitSingleFactor(MiniRustParser.SingleFactorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterNumber(MiniRustParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitNumber(MiniRustParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterParentheses(MiniRustParser.ParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitParentheses(MiniRustParser.ParenthesesContext ctx);
}
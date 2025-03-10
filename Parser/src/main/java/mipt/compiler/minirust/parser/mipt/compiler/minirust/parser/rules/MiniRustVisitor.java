// Generated from D:/GitReps/compiler_course/Parser/src/main/java/mipt/compiler/minirust/parser/rules/MiniRust.g4 by ANTLR 4.13.2
package mipt.compiler.minirust.parser.rules;
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
	 * Visit a parse tree produced by the {@code SingleTerm}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleTerm(MiniRustParser.SingleTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link MiniRustParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(MiniRustParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(MiniRustParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SingleFactor}
	 * labeled alternative in {@link MiniRustParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleFactor(MiniRustParser.SingleFactorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(MiniRustParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link MiniRustParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentheses(MiniRustParser.ParenthesesContext ctx);
}
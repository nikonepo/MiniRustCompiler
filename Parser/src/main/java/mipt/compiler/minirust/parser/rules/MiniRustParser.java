// Generated from D:/GitReps/compiler_course/Parser/src/main/java/mipt/compiler/minirust/parser/rules/MiniRust.g4 by ANTLR 4.13.2
package mipt.compiler.minirust.parser.rules;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class MiniRustParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, NUMBER = 7, WS = 8;
    public static final int
            RULE_expr = 0, RULE_term = 1, RULE_factor = 2;

    private static String[] makeRuleNames() {
        return new String[]{
                "expr", "term", "factor"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'+'", "'-'", "'*'", "'/'", "'('", "')'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, "NUMBER", "WS"
        };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "MiniRust.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public MiniRustParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ExprContext extends ParserRuleContext {
        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        public ExprContext() {
        }

        public void copyFrom(ExprContext ctx) {
            super.copyFrom(ctx);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SingleTermContext extends ExprContext {
        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public SingleTermContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterSingleTerm(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitSingleTerm(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor)
                return ((MiniRustVisitor<? extends T>) visitor).visitSingleTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class AddSubContext extends ExprContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public AddSubContext(ExprContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterAddSub(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitAddSub(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor) return ((MiniRustVisitor<? extends T>) visitor).visitAddSub(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 0;
        enterRecursionRule(_localctx, 0, RULE_expr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    _localctx = new SingleTermContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;

                    setState(7);
                    term(0);
                }
                _ctx.stop = _input.LT(-1);
                setState(14);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new AddSubContext(new ExprContext(_parentctx, _parentState));
                                pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                setState(9);
                                if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                setState(10);
                                _la = _input.LA(1);
                                if (!(_la == T__0 || _la == T__1)) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                                setState(11);
                                term(0);
                            }
                        }
                    }
                    setState(16);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class TermContext extends ParserRuleContext {
        public TermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_term;
        }

        public TermContext() {
        }

        public void copyFrom(TermContext ctx) {
            super.copyFrom(ctx);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class MulDivContext extends TermContext {
        public TermContext term() {
            return getRuleContext(TermContext.class, 0);
        }

        public FactorContext factor() {
            return getRuleContext(FactorContext.class, 0);
        }

        public MulDivContext(TermContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterMulDiv(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitMulDiv(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor) return ((MiniRustVisitor<? extends T>) visitor).visitMulDiv(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SingleFactorContext extends TermContext {
        public FactorContext factor() {
            return getRuleContext(FactorContext.class, 0);
        }

        public SingleFactorContext(TermContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterSingleFactor(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitSingleFactor(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor)
                return ((MiniRustVisitor<? extends T>) visitor).visitSingleFactor(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TermContext term() throws RecognitionException {
        return term(0);
    }

    private TermContext term(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        TermContext _localctx = new TermContext(_ctx, _parentState);
        TermContext _prevctx = _localctx;
        int _startState = 2;
        enterRecursionRule(_localctx, 2, RULE_term, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    _localctx = new SingleFactorContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;

                    setState(18);
                    factor();
                }
                _ctx.stop = _input.LT(-1);
                setState(25);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new MulDivContext(new TermContext(_parentctx, _parentState));
                                pushNewRecursionContext(_localctx, _startState, RULE_term);
                                setState(20);
                                if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                setState(21);
                                _la = _input.LA(1);
                                if (!(_la == T__2 || _la == T__3)) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                                setState(22);
                                factor();
                            }
                        }
                    }
                    setState(27);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class FactorContext extends ParserRuleContext {
        public FactorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_factor;
        }

        public FactorContext() {
        }

        public void copyFrom(FactorContext ctx) {
            super.copyFrom(ctx);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NumberContext extends FactorContext {
        public TerminalNode NUMBER() {
            return getToken(MiniRustParser.NUMBER, 0);
        }

        public NumberContext(FactorContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor) return ((MiniRustVisitor<? extends T>) visitor).visitNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ParenthesesContext extends FactorContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public ParenthesesContext(FactorContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).enterParentheses(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof MiniRustListener) ((MiniRustListener) listener).exitParentheses(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof MiniRustVisitor)
                return ((MiniRustVisitor<? extends T>) visitor).visitParentheses(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FactorContext factor() throws RecognitionException {
        FactorContext _localctx = new FactorContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_factor);
        try {
            setState(33);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NUMBER:
                    _localctx = new NumberContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                {
                    setState(28);
                    match(NUMBER);
                }
                break;
                case T__4:
                    _localctx = new ParenthesesContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                {
                    setState(29);
                    match(T__4);
                    setState(30);
                    expr(0);
                    setState(31);
                    match(T__5);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 0:
                return expr_sempred((ExprContext) _localctx, predIndex);
            case 1:
                return term_sempred((TermContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 2);
        }
        return true;
    }

    private boolean term_sempred(TermContext _localctx, int predIndex) {
        switch (predIndex) {
            case 1:
                return precpred(_ctx, 2);
        }
        return true;
    }

    public static final String _serializedATN =
            "\u0004\u0001\b$\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
                    "\u0002\u0007\u0002\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001" +
                    "\u0000\u0001\u0000\u0005\u0000\r\b\u0000\n\u0000\f\u0000\u0010\t\u0000" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
                    "\u0005\u0001\u0018\b\u0001\n\u0001\f\u0001\u001b\t\u0001\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\"\b\u0002\u0001" +
                    "\u0002\u0000\u0002\u0000\u0002\u0003\u0000\u0002\u0004\u0000\u0002\u0001" +
                    "\u0000\u0001\u0002\u0001\u0000\u0003\u0004#\u0000\u0006\u0001\u0000\u0000" +
                    "\u0000\u0002\u0011\u0001\u0000\u0000\u0000\u0004!\u0001\u0000\u0000\u0000" +
                    "\u0006\u0007\u0006\u0000\uffff\uffff\u0000\u0007\b\u0003\u0002\u0001\u0000" +
                    "\b\u000e\u0001\u0000\u0000\u0000\t\n\n\u0002\u0000\u0000\n\u000b\u0007" +
                    "\u0000\u0000\u0000\u000b\r\u0003\u0002\u0001\u0000\f\t\u0001\u0000\u0000" +
                    "\u0000\r\u0010\u0001\u0000\u0000\u0000\u000e\f\u0001\u0000\u0000\u0000" +
                    "\u000e\u000f\u0001\u0000\u0000\u0000\u000f\u0001\u0001\u0000\u0000\u0000" +
                    "\u0010\u000e\u0001\u0000\u0000\u0000\u0011\u0012\u0006\u0001\uffff\uffff" +
                    "\u0000\u0012\u0013\u0003\u0004\u0002\u0000\u0013\u0019\u0001\u0000\u0000" +
                    "\u0000\u0014\u0015\n\u0002\u0000\u0000\u0015\u0016\u0007\u0001\u0000\u0000" +
                    "\u0016\u0018\u0003\u0004\u0002\u0000\u0017\u0014\u0001\u0000\u0000\u0000" +
                    "\u0018\u001b\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000" +
                    "\u0019\u001a\u0001\u0000\u0000\u0000\u001a\u0003\u0001\u0000\u0000\u0000" +
                    "\u001b\u0019\u0001\u0000\u0000\u0000\u001c\"\u0005\u0007\u0000\u0000\u001d" +
                    "\u001e\u0005\u0005\u0000\u0000\u001e\u001f\u0003\u0000\u0000\u0000\u001f" +
                    " \u0005\u0006\u0000\u0000 \"\u0001\u0000\u0000\u0000!\u001c\u0001\u0000" +
                    "\u0000\u0000!\u001d\u0001\u0000\u0000\u0000\"\u0005\u0001\u0000\u0000" +
                    "\u0000\u0003\u000e\u0019!";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
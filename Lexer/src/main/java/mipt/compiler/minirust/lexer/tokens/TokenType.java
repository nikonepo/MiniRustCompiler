package mipt.compiler.minirust.lexer.tokens;

public sealed interface TokenType
    permits DelimitersToken, IdentifierToken, IntegerLiteral, KeywordsToken, PunctuationsToken {}

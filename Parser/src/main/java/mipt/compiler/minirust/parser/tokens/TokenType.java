package mipt.compiler.minirust.parser.tokens;

public sealed interface TokenType
    permits DelimitersToken, IdentifierToken, IntegerLiteral, KeywordsToken, PunctuationsToken {}

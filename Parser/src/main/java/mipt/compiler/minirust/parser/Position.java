package mipt.compiler.minirust.parser;

import mipt.compiler.minirust.parser.Tokenizer.ParserPosition;

public final class Position {

    public int column = 0;
    public int line = 0;

    public Position(int column, int line) {
        this.column = column;
        this.line = line;
    }

    public Position(ParserPosition parserPosition) {
        this.column = parserPosition.column;
        this.line = parserPosition.line;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Position otherPosition)) {
            return false;
        }

        return column == otherPosition.column && line == otherPosition.line;
    }
}

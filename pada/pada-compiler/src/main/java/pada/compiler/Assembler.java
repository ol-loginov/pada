package pada.compiler;

import org.antlr.v4.runtime.*;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Assembler {
    private final List<SourceError> errorList = new ArrayList<SourceError>();

    public void addUnit(Source source) {
        CharStream charStream;
        try {
            charStream = new ANTLRInputStream(source.read());
        } catch (IOException e) {
            throw new IllegalArgumentException("source is na", e);
        }

        PadaLexer lexer = new PadaLexer(charStream);
        PadaParser parser = new PadaParser(new BufferedTokenStream(lexer));
        parser.addErrorListener(createErrorListener(source.getPath()));

        PadaParser.UnitContext unit = parser.unit();
    }

    private ANTLRErrorListener createErrorListener(final String path) {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                SourceError error = new SourceError();
                error.path = path;
                error.line = line;
                error.column = charPositionInLine;
                error.message = msg;
                errorList.add(error);
            }
        };
    }

    public int getErrorCount() {
        return errorList.size();
    }
}

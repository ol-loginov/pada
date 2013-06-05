import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.junit.Test;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;

public class TestRun {
    @Test
    public void testAssetAll() throws IOException {
        PadaLexer padaLexer = new PadaLexer(new ANTLRInputStream(getClass().getResourceAsStream("0000.pada")));
        PadaParser padaParser = new PadaParser(new BufferedTokenStream(padaLexer));
        PadaParser.CompilationUnitContext res = padaParser.compilationUnit();
        res = null;
    }
}

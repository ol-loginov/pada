import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.junit.Test;
import pada.compiler.CodeTreeWriter;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class TestRun {
    @Test
    public void testAssetAll() throws IOException {
        PadaLexer padaLexer = new PadaLexer(new ANTLRInputStream(getClass().getResourceAsStream("0000.pada")));
        PadaParser padaParser = new PadaParser(new BufferedTokenStream(padaLexer));
        padaParser.compilationUnit().accept(new CodeTreeWriter(new OutputStreamWriter(System.out)));
    }
}

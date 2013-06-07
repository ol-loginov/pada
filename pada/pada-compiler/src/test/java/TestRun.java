import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.junit.Test;
import pada.compiler.CodeTreeWriter;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestRun {
    @Test
    public void testAssetAll() throws IOException, URISyntaxException {
        int assetIndex = -1;
        while (TestHelper.hasAsset(String.format("%04d.pada", ++assetIndex))) {
            testAsset(String.format("%04d", assetIndex));
        }
        assertEquals("some tests should present in assets", assetIndex, 4);
    }

    @Test
    public void testAsset() throws IOException, URISyntaxException {
        testAsset("0003");
    }

    private void testAsset(String assetName) throws URISyntaxException, IOException {
        PadaLexer lexer = new PadaLexer(new ANTLRInputStream(getClass().getResourceAsStream(assetName + ".pada")));
        PadaParser parser = new PadaParser(new BufferedTokenStream(lexer));
        PadaParser.CompilationUnitContext unit = parser.compilationUnit();

        String expected = TestHelper.readAsset(assetName + ".tree");
        String actual = unit.accept(new CodeTreeWriter<>(new StringWriter())).toString();
        assertEquals("validate tree of " + assetName, expected, actual);
    }
}

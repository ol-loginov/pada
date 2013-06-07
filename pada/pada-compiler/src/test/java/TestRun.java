import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.junit.Test;
import pada.compiler.UnitErrorListener;
import pada.compiler.UnitTreeWriter;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class TestRun {
    @Test
    public void testAssetAll() throws IOException, URISyntaxException {
        int assetIndex = -1;
        while (TestHelper.hasAsset(String.format("%04d.pada", ++assetIndex))) {
            testAsset(String.format("%04d", assetIndex));
        }
        assertEquals("some tests should present in assets", assetIndex, 6);
    }

    @Test
    public void testAsset() throws IOException, URISyntaxException {
        testAsset("0005");
    }

    // /home/oregu/Projects/pada/github/pada/pada-compiler/src/test/java/TestRun.java:[38,36] ')' expected
    private void testAsset(String assetName) throws URISyntaxException, IOException {
        String fileName = assetName + ".pada";

        UnitErrorListener parserError = new UnitErrorListener(fileName);
        PadaLexer lexer = new PadaLexer(new ANTLRInputStream(getClass().getResourceAsStream(fileName)));
        PadaParser parser = new PadaParser(new BufferedTokenStream(lexer));
        parser.addErrorListener(parserError);
        PadaParser.UnitContext unit = parser.unit();

        String expected = TestHelper.readAsset(assetName + ".tree");
        String actual = unit.accept(new UnitTreeWriter<>(new StringWriter())).toString();
        assertEquals("validate tree of " + assetName, expected, actual + TestHelper.join(parserError.getErrorList(), "\n"));
    }
}

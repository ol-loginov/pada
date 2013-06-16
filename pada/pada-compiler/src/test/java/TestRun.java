import org.junit.Test;
import pada.compiler.Assembler;
import pada.compiler.SourceDelegate;

import java.io.IOException;
import java.io.InputStream;
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
        testAsset("0006");
    }

    // /home/oregu/Projects/pada/github/pada/pada-compiler/src/test/java/TestRun.java:[38,36] ')' expected
    private void testAsset(String assetName) throws URISyntaxException, IOException {
        String sourceName = assetName + ".pada";
        InputStream source = getClass().getResourceAsStream(sourceName);

        Assembler assembler = new Assembler();
        assembler.addUnit(new SourceDelegate(source, sourceName));

        assertEquals(0, assembler.getErrorCount());

//        PadaParser.UnitContext unit = parser.unit();
//
//        UnitCodeGenerator sourceGenerator = new UnitCodeGenerator();
//
//        String expected = TestHelper.readAsset(assetName + ".tree");
//        String actual = "";
//        assertEquals("validate tree of " + assetName, expected, actual + TestHelper.join(parserError.getErrorList(), "\n"));
    }
}

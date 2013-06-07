import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class TestHelper {
    public static String join(Iterable<String> strings, String delimiter) {
        StringBuilder res = new StringBuilder();
        for (String s : strings) {
            res.append(delimiter);
            res.append(s);
        }
        return res.toString();
    }

    public static String readAsset(String path) throws IOException {
        InputStream is = TestHelper.class.getResourceAsStream(path);
        if (is == null) throw new IllegalArgumentException("asset " + path + " is not found");

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is, Charset.forName("utf-8"))) {
            CharBuffer buffer = CharBuffer.allocate(1024 * 100);
            do {
                int bufferLength = buffer.position();
                buffer.rewind();
                stringBuilder.append(buffer.subSequence(0, bufferLength));
            } while (isr.read(buffer) > 0);
        }
        return stringBuilder.toString();
    }

    public static boolean hasAsset(String path) {
        try (InputStream resource = TestHelper.class.getResourceAsStream(path)) {
            return resource != null;
        } catch (IOException e) {
            return false;
        }
    }
}

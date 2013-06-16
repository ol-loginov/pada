package pada.compiler;

import java.io.InputStream;

public class SourceDelegate implements Source {
    private final InputStream inputStream;
    private final String path;

    public SourceDelegate(InputStream inputStream, String path) {
        this.inputStream = inputStream;
        this.path = path;
    }

    @Override
    public InputStream read() {
        return inputStream;
    }

    @Override
    public String getPath() {
        return path;
    }
}

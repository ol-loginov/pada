package pada.compiler;

import java.io.InputStream;

public interface Source {
    InputStream read();

    String getPath();
}

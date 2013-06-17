package pada.ide.idea;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

public class PadaFileTypeFactory extends FileTypeFactory {
    public void createFileTypes(FileTypeConsumer consumer) {
        consumer.consume(PadaFileType.INSTANCE, PadaFileType.DEFAULT_EXTENSION);
    }
}

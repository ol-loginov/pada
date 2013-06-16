package pada.ide.idea;

public class PadaFileTypeFactory extends FileTypeFactory {
    public void createFileTypes(FileTypeConsumer consumer) {
        consumer.consume(PadaFile.INSTANCE, PadaFile.DEFAULT_EXTENSION);
    }
}

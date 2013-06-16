package pada.ide.idea;

import javax.swing.*;
import java.nio.charset.Charset;

public class PadaFile extends LanguageFileType {
    public static final PadaFile INSTANCE = new PadaFile();
    public static final String TYPE_NAME = "Pada";
    public static final String DEFAULT_EXTENSION = "pada";
    public static final String DOT_DEFAULT_EXTENSION = "." + DEFAULT_EXTENSION;

    private PadaFile() {
        super(PadaLanguage.INSTANCE);
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public String getDescription() {
        return Res.message("pada.files.file.type.description");
    }


    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public Icon getIcon() {
        return AllIcons.FileTypes.Text;
    }

    public String getCharset(VirtualFile file, final byte[] content) {
        Charset charset = EncodingManager.getInstance().getDefaultCharsetForPropertiesFiles(file);
        String defaultCharsetName = charset == null ? CharsetToolkit.getDefaultSystemCharset().name() : charset.name();
        return defaultCharsetName;
    }
}


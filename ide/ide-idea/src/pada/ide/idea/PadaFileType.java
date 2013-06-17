package pada.ide.idea;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingManager;

import javax.swing.*;
import java.nio.charset.Charset;

public class PadaFileType extends LanguageFileType {
    public static final PadaFileType INSTANCE = new PadaFileType();
    public static final String TYPE_NAME = "Pada";
    public static final String DEFAULT_EXTENSION = "pada";
    public static final String DOT_DEFAULT_EXTENSION = "." + DEFAULT_EXTENSION;

    private PadaFileType() {
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
        return charset == null ? CharsetToolkit.getDefaultSystemCharset().name() : charset.name();
    }
}


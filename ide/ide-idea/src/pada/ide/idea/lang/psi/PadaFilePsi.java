package pada.ide.idea.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaFileType;
import pada.ide.idea.PadaLanguage;

public class PadaFilePsi extends PsiFileBase implements PsiFile {
    public PadaFilePsi(FileViewProvider viewProvider) {
        super(viewProvider, PadaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return PadaFileType.INSTANCE;
    }
}

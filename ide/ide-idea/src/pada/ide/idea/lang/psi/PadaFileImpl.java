package pada.ide.idea.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaFileType;
import pada.ide.idea.PadaLanguage;

public class PadaFileImpl extends PsiFileBase implements PadaFile {
    public PadaFileImpl(FileViewProvider viewProvider) {
        super(viewProvider, PadaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public PsiClass[] getClasses() {
        return new PsiClass[0];
    }

    @Override
    public String getPackageName() {
        return "";
    }

    @Override
    public void setPackageName(String packageName) {
    }

    @Override
    public boolean importClass(PsiClass aClass) {
        return false;
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return PadaFileType.INSTANCE;
    }
}

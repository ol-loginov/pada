package pada.ide.idea.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import pada.compiler.ast.PadaUnit;
import pada.compiler.ast.PadaUnitOwner;
import pada.ide.idea.PadaFileType;
import pada.ide.idea.PadaLanguage;

public class PadaFilePsi extends PsiFileBase implements PadaUnitOwner, PsiFile {
    private PadaUnit padaUnit;

    public PadaFilePsi(FileViewProvider viewProvider) {
        super(viewProvider, PadaLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return PadaFileType.INSTANCE;
    }

    @Override
    public PadaUnit getUnit() {
        return padaUnit;
    }

    @Override
    public void setUnit(PadaUnit unit) {
        padaUnit = unit;
    }
}

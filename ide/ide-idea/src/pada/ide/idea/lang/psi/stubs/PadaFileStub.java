package pada.ide.idea.lang.psi.stubs;

import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import pada.ide.idea.lang.PadaParserDefinition;

public class PadaFileStub extends PsiFileStubImpl<PsiFile> {
    public PadaFileStub(PsiFile file) {
        super(file);
    }

    @Override
    public IStubFileElementType getType() {
        return PadaParserDefinition.PADA_FILE;
    }
}

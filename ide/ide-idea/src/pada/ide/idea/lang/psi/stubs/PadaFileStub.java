package pada.ide.idea.lang.psi.stubs;

import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import pada.ide.idea.lang.PadaParserDefinition;
import pada.ide.idea.lang.psi.PadaFilePsi;

public class PadaFileStub extends PsiFileStubImpl<PadaFilePsi> {
    public PadaFileStub(PadaFilePsi file) {
        super(file);
    }

    @Override
    public IStubFileElementType getType() {
        return PadaParserDefinition.PADA_FILE;
    }
}

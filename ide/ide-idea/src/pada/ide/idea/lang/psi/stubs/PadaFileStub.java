package pada.ide.idea.lang.psi.stubs;

import com.intellij.psi.stubs.PsiFileStubImpl;
import com.intellij.psi.tree.IStubFileElementType;
import pada.ide.idea.PadaLanguage;
import pada.ide.idea.lang.psi.PadaFilePsi;

public class PadaFileStub extends PsiFileStubImpl<PadaFilePsi> {
    public static final IStubFileElementType<PadaFileStub> TYPE = new IStubFileElementType<PadaFileStub>(PadaLanguage.INSTANCE);

    public PadaFileStub(PadaFilePsi file) {
        super(file);
    }

    @Override
    public IStubFileElementType getType() {
        return TYPE;
    }
}

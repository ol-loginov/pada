package pada.ide.idea.lang.psi.stubs;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NonNls;
import pada.ide.idea.lang.psi.stubs.PadaFileStub;

public class PadaFileStubElementType extends IStubFileElementType<PadaFileStub> {
    public PadaFileStubElementType(Language language) {
        super(language);
    }

    public PadaFileStubElementType(@NonNls String debugName, Language language) {
        super(debugName, language);
    }
}

package pada.ide.idea.lang.psi.stubs.elements;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NonNls;
import pada.ide.idea.lang.psi.stubs.PadaFileStub;

public class PadaStubFileElementType extends IStubFileElementType<PadaFileStub> {
    public PadaStubFileElementType(Language language) {
        super(language);
    }

    public PadaStubFileElementType(@NonNls String debugName, Language language) {
        super(debugName, language);
    }
}

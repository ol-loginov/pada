package pada.ide.idea.lang.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaLanguage;

public abstract class PadaPsi<T extends StubElement> extends StubBasedPsiElementBase<T> {
    public PadaPsi(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public PadaPsi(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public Language getLanguage() {
        return PadaLanguage.INSTANCE;
    }
}

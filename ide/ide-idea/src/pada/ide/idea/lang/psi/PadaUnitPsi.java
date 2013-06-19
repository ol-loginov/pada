package pada.ide.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;

public class PadaUnitPsi extends PadaPsi {
    public PadaUnitPsi(@NotNull StubElement stub, @NotNull IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public PadaUnitPsi(@NotNull ASTNode node) {
        super(node);
    }
}

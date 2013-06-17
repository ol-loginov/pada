package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaLanguage;

public class PadaElement extends IElementType {
    public PadaElement(@NotNull @NonNls String debugName) {
        super(debugName, PadaLanguage.INSTANCE);
    }
}

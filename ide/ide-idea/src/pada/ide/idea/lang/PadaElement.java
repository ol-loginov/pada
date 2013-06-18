package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaLanguage;

public class PadaElement extends IElementType {
    private final int ruleIndex;

    public PadaElement(@NotNull @NonNls String debugName, int ruleIndex) {
        super(debugName, PadaLanguage.INSTANCE);
        this.ruleIndex = ruleIndex;
    }

    public int getRuleIndex() {
        return ruleIndex;
    }
}

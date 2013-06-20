package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaLanguage;

public class LangElement extends IElementType {
    public static final LangElement KEYWORD = new LangElement("KEYWORD");
    public static final LangElement UNIT = new LangElement("UNIT");

    public LangElement(@NotNull @NonNls String debugName) {
        super(debugName, PadaLanguage.INSTANCE);
    }
}

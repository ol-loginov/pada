package pada.ide.idea.lang.psi;

import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import pada.ide.idea.PadaLanguage;

public abstract class PadaPsi extends LightElement {
    public PadaPsi(PsiManager manager) {
        super(manager, PadaLanguage.INSTANCE);
    }
}

package pada.ide.idea.lang.psi;

import com.intellij.psi.PsiManager;

public class PadaUnitPsi extends PadaPsi {
    public PadaUnitPsi(PsiManager manager) {
        super(manager);
    }

    @Override
    public String toString() {
        return "Unit";
    }
}

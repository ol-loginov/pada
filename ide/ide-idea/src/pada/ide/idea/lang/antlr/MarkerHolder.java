package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import pada.util.ObjectHolder;

public class MarkerHolder extends ObjectHolder<PsiBuilder.Marker> {
    public void set(PsiBuilder builder) {
        this.value = builder.mark();
    }

    public void error(String text) {
        this.value.error(text);
        this.value = null;
    }

    public void done(IElementType element) {
        this.value.done(element);
        this.value = null;
    }

    public void collapse(IElementType element) {
        this.value.collapse(element);
        this.value = null;
    }
}

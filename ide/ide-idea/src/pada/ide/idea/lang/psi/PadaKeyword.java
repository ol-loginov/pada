package pada.ide.idea.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ForeignLeafType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.lang.LangElement;

public class PadaKeyword extends LeafPsiElement {
    public static IElementType typeOf(CharSequence text) {
        return new ForeignLeafType(LangElement.KEYWORD, text) {
            @NotNull
            @Override
            public ASTNode createLeafNode(CharSequence leafText) {
                return new PadaKeyword(leafText);
            }
        };
    }

    public PadaKeyword(CharSequence text) {
        super(LangElement.KEYWORD, text);
    }

    @Override
    public String toString() {
        return "Keyword: " + getText();
    }
}

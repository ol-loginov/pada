package pada.ide.idea.lang;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

public class PadaPsiResolver {
    public static PsiElement createElement(ASTNode node) {
        IElementType elem = node.getElementType();
        return new ASTWrapperPsiElement(node);
    }
}

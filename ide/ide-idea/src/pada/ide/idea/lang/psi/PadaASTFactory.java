package pada.ide.idea.lang.psi;

public class PadaASTFactory extends ASTFactory {
    public CompositeElement createComposite(final IElementType type) {
        if (type instanceof IFileElementType) {
            return new FileElement(type, null);
        }
        return new CompositeElement(type);
    }

    public LeafElement createLeaf(final IElementType type, CharSequence text) {
        if (type == PropertiesTokenTypes.VALUE_CHARACTERS) {
            return new PropertyValueImpl(type, text);
        }

        if (type == PropertiesTokenTypes.END_OF_LINE_COMMENT) {
            return new PsiCommentImpl(type, text);
        }

        return new LeafPsiElement(type, text);
    }
}

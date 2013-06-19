package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.PadaLanguage;

public class LangToken extends IElementType {
    private static final LangToken[] TOKENS = new LangToken[PadaLexer.tokenNames.length];

    static {
        for (int i = 0; i < PadaLexer.tokenNames.length; ++i) {
            TOKENS[i] = new LangToken(PadaLexer.tokenNames[i]);
        }
    }

    public static LangToken findByAntlrType(int antlrType) {
        if (antlrType == -1)
            return null;
        return TOKENS[antlrType];
    }

    public LangToken(String debugName) {
        super(debugName, PadaLanguage.INSTANCE);
    }
}

package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.PadaLanguage;

public final class LangTokens {
    private static final LangTokens instance = new LangTokens();
    private final PadaToken[] tokens = new PadaToken[PadaLexer.tokenNames.length];

    private LangTokens() {
        for (int i = 0; i < PadaLexer.tokenNames.length; ++i) {
            tokens[i] = new PadaToken(PadaLexer.tokenNames[i]);
        }
    }

    public PadaToken findByAntlrType(int antlrType) {
        if (antlrType == -1)
            return null;
        return tokens[antlrType];
    }

    public static LangTokens instance() {
        return instance;
    }

    public static class PadaToken extends IElementType {
        //private final Token antlrToken;

        public PadaToken(String debugName) {
            super(debugName, PadaLanguage.INSTANCE);
        }

//        private PadaToken(String debugName, Token antlrToken) {
//            super(debugName, PadaLanguage.INSTANCE, antlrToken == null);
//            this.antlrToken = antlrToken;
//        }
//
//        public Token getAntlrToken() {
//            return antlrToken;
//        }
//
//        public PadaToken assignToken(Token token) {
//            return new PadaToken(toString(), token);
//        }
    }
}

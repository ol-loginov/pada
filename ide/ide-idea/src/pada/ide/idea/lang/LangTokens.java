package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.Token;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.PadaLanguage;

public final class LangTokens {
    private static final LangTokens instance = new LangTokens();
    private final PadaToken[] tokens = new PadaToken[PadaLexer.tokenNames.length];

    private LangTokens() {
        for (int i = 0; i < PadaLexer.tokenNames.length; ++i) {
            tokens[i] = new PadaToken(PadaLexer.tokenNames[i], i);
        }
        findByAntlrType(PadaLexer.Space).skip();
        findByAntlrType(PadaLexer.Comment).skip();
        findByAntlrType(PadaLexer.LineComment).skip();
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
        private final int antlrType;
        private int antlrChannel = Token.DEFAULT_CHANNEL;

        public PadaToken(String debugName, int antlrType) {
            super(debugName, PadaLanguage.INSTANCE);
            this.antlrType = antlrType;
        }

        public int getAntlrType() {
            return antlrType;
        }

        public int getAntlrChannel() {
            return antlrChannel;
        }

        public PadaToken skip() {
            this.antlrChannel = Token.HIDDEN_CHANNEL;
            return this;
        }
    }
}

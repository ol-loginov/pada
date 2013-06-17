package pada.ide.idea.lang;

import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.Token;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.PadaLanguage;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public interface LangTokens {
    IdeaToken Space = new IdeaToken("Space", PadaLexer.Space).skip();
    IdeaToken Comment = new IdeaToken("Comment", PadaLexer.Comment).skip();
    IdeaToken LineComment = new IdeaToken("LineComment", PadaLexer.LineComment).skip();
    IdeaToken CharacterLiteral = new IdeaToken("CharacterLiteral", PadaLexer.CharacterLiteral);
    IdeaToken StringLiteral = new IdeaToken("StringLiteral", PadaLexer.StringLiteral);
    IdeaToken HexLiteral = new IdeaToken("HexLiteral", PadaLexer.HexLiteral);
    IdeaToken DecimalLiteral = new IdeaToken("DecimalLiteral", PadaLexer.DecimalLiteral);
    IdeaToken OctalLiteral = new IdeaToken("OctalLiteral", PadaLexer.OctalLiteral);
    IdeaToken BinaryLiteral = new IdeaToken("BinaryLiteral", PadaLexer.BinaryLiteral);
    IdeaToken FloatingPointLiteral = new IdeaToken("FloatingPointLiteral", PadaLexer.FloatingPointLiteral);
    IdeaToken Identifier = new IdeaToken("Identifier", PadaLexer.Identifier);

    public static class Factory {
        private final static Map<Integer, IdeaToken> tokens = new HashMap<Integer, IdeaToken>();

        static {
            for (Field field : LangTokens.class.getFields()) {
                try {
                    IdeaToken token = (IdeaToken) field.get(null);
                    tokens.put(token.getAntlrType(), token);
                } catch (IllegalAccessException e) {
                }
            }
        }

        public static IdeaToken findByAntlrType(int antlrType) {
            return tokens.get(antlrType);
        }
    }

    public static class IdeaToken extends IElementType {
        private final int antlrType;
        private int antlrChannel = Token.DEFAULT_CHANNEL;

        public IdeaToken(String debugName, int antlrType) {
            super(debugName, PadaLanguage.INSTANCE);
            this.antlrType = antlrType;
        }

        public int getAntlrType() {
            return antlrType;
        }

        public int getAntlrChannel() {
            return antlrChannel;
        }

        public IdeaToken skip() {
            this.antlrChannel = Token.HIDDEN_CHANNEL;
            return this;
        }
    }
}

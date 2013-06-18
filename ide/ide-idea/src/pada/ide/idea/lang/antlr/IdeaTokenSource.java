package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Pair;
import pada.ide.idea.lang.LangTokens;

public class IdeaTokenSource implements TokenSource {
    private final PsiBuilder builder;
    private boolean doAdvance = false;

    public IdeaTokenSource(PsiBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Token nextToken() {
        if (doAdvance)
            builder.advanceLexer();
        doAdvance = true;

        LangTokens.PadaToken ideaToken = (LangTokens.PadaToken) builder.getTokenType();
        if (ideaToken == null) {
            CommonToken token = getTokenFactory().create(Token.EOF, null);
            token.setStartIndex(builder.getCurrentOffset());
            token.setStopIndex(builder.getCurrentOffset());
            return token;
        }

        String tokenText = builder.getTokenText();
        if (tokenText == null)
            tokenText = "";

        return getTokenFactory().create(new Pair<TokenSource, CharStream>(this, null),
                ideaToken.getAntlrType(),
                tokenText,
                Token.DEFAULT_CHANNEL,
                builder.getCurrentOffset(),
                builder.getCurrentOffset() + tokenText.length(),
                0, 0);
    }

    @Override
    public int getLine() {
        return -1;
    }

    @Override
    public int getCharPositionInLine() {
        return -1;
    }

    @Override
    public CharStream getInputStream() {
        return null;
    }

    @Override
    public String getSourceName() {
        return "memory";
    }

    @Override
    public void setTokenFactory(@NotNull TokenFactory<?> factory) {
    }

    @Override
    public TokenFactory<CommonToken> getTokenFactory() {
        return CommonTokenFactory.DEFAULT;
    }
}

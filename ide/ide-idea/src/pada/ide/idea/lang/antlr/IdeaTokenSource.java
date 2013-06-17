package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import pada.ide.idea.lang.LangTokens;

public class IdeaTokenSource implements TokenSource {
    private final PsiBuilder builder;

    public IdeaTokenSource(PsiBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Token nextToken() {
        LangTokens.IdeaToken ideaToken = (LangTokens.IdeaToken) builder.getTokenType();
        if (ideaToken == null) {
            CommonToken token = getTokenFactory().create(Token.EOF, null);
            token.setStartIndex(builder.getCurrentOffset());
            token.setStopIndex(builder.getCurrentOffset());
            return token;
        }

        CommonToken token = new CommonToken(ideaToken.getAntlrType());
        token.setChannel(ideaToken.getAntlrChannel());
        token.setText(builder.getTokenText());
        token.setStartIndex(builder.getCurrentOffset());
        try {
            return token;
        } finally {
            builder.advanceLexer();
            token.setStopIndex(builder.getCurrentOffset());
        }
    }

    @Override
    public int getLine() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public int getCharPositionInLine() {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public CharStream getInputStream() {
        throw new IllegalStateException("not implemented");
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

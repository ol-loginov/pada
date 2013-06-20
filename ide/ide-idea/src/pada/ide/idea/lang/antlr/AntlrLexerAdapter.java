package pada.ide.idea.lang.antlr;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.jetbrains.annotations.Nullable;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.lang.LangToken;

import java.io.IOException;

public class AntlrLexerAdapter extends LexerBase {
    private CharSequence buffer;
    private Interval interval;
    private int state;

    Token token;
    PadaLexer delegate = new PadaLexer(new ANTLRInputStream()) {
        @Override
        public void skip() {
            // you shouldn't skip any token
        }
    };

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.state = initialState;
        this.buffer = buffer;
        this.interval = new Interval(startOffset, endOffset);
        this.token = null;
        if (startOffset > endOffset)
            return;

        try {
            CharSequenceReader bufferReader = new CharSequenceReader(buffer.subSequence(startOffset, endOffset));
            delegate.setInputStream(new ANTLRInputStream(bufferReader));
        } catch (IOException e) {
            throw new IllegalStateException("cannot create ANTLR input stream", e);
        }

        advance();
    }

    @Override
    public int getState() {
        return state;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        if (token == null || token.getType() == Token.EOF)
            return null;
        return LangToken.findByAntlrType(token.getType());
    }

    @Override
    public int getTokenStart() {
        return token.getStartIndex() + getBufferStart();
    }

    @Override
    public int getTokenEnd() {
        return token.getStopIndex() + getBufferStart();
    }

    @Override
    public void advance() {
        if (delegate == null) return;
        token = delegate.nextToken();
    }

    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    public int getBufferStart() {
        return interval.a;
    }

    @Override
    public int getBufferEnd() {
        return interval.b;
    }
}

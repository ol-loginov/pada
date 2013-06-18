package pada.ide.idea.lang.antlr;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Nullable;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.lang.LangTokens;

import java.io.IOException;

public class AntlrLexerAdapter extends LexerBase {
    private PadaLexer delegate;

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;

    int state;
    Token token;

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.state = initialState;
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.delegate = new PadaLexer(getCharStream());
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
        return LangTokens.instance().findByAntlrType(token.getType());
    }

    @Override
    public int getTokenStart() {
        return token.getStartIndex();
    }

    @Override
    public int getTokenEnd() {
        return token.getStopIndex();
    }

    @Override
    public void advance() {
        token = delegate.nextToken();
    }

    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    public CharStream getCharStream() {
        try {
            return new ANTLRInputStream(new CharSequenceReader(buffer.subSequence(getBufferStart(), getBufferEnd())));
        } catch (IOException e) {
            throw new IllegalStateException("cannot create ANTLR input stream", e);
        }
    }

    public int getBufferStart() {
        return startOffset;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}
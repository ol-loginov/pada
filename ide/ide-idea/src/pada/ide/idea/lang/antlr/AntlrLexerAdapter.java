package pada.ide.idea.lang.antlr;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.jetbrains.annotations.Nullable;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.lang.LangToken;

import java.io.IOException;

public class AntlrLexerAdapter extends LexerBase {
    private CharSequence buffer;
    private int bufferState;
    private Interval interval;

    final PadaLexer delegate = new PadaLexer(new ANTLRInputStream()) {
        @Override
        public void skip() {
            // you shouldn't skip any token
        }

        // test text: import a.b."a
        @Override
        public void recover(LexerNoViableAltException e) {
            if (_input.LA(1) != IntStream.EOF) {
                // skip a char and try again
                getInterpreter().consume(_input);
            }

        }
    };
    Token token;

    @Override
    public void start(CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.bufferState = initialState;
        this.interval = new Interval(startOffset, endOffset);
        this.token = null;
        if (isBufferEmpty())
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
        return bufferState;
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
        return getBufferStart() + token.getStartIndex();
    }

    @Override
    public int getTokenEnd() {
        return getBufferStart() + token.getStopIndex() + 1;
    }

    @Override
    public void advance() {
        token = isBufferEmpty() ? null : delegate.nextToken();
    }

    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    private boolean isBufferEmpty() {
        return getBufferLength() <= 0;
    }

    private int getBufferLength() {
        return interval.b - interval.a;
    }

    public int getBufferStart() {
        return interval.a;
    }

    @Override
    public int getBufferEnd() {
        return interval.b;
    }
}

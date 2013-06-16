package pada.ide.idea.lang;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class PadaLexer extends LexerBase {
    pada.compiler.antlr4.PadaLexer delegate;

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
        try {
            this.delegate = new pada.compiler.antlr4.PadaLexer(new ANTLRInputStream(new CharSequenceReader(buffer.subSequence(startOffset, endOffset))));
        } catch (IOException e) {
            throw new IllegalStateException("cannot lexer", e);
        }
    }

    @Override
    public int getState() {
        return state;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        if (token == null)
            return null;
        switch (token.getType()) {
            case pada.compiler.antlr4.PadaLexer.BinaryLiteral:
                return TokenType.WHITE_SPACE;
        }
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
        token = delegate.getToken();
    }

    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}

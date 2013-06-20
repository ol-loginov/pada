package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.text.CharSequenceReader;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.psi.stubs.PadaFileStub;

import java.io.IOException;

public class AntlrParserAdapter implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        PsiBuilder.Marker fileMarker = builder.mark();
        parseRoot(builder);
        fileMarker.done(PadaFileStub.TYPE);

        return builder.getTreeBuilt();
    }

    private void parseRoot(PsiBuilder builder) {
        PadaParser parser = createParser(builder);
        parser.removeErrorListeners();
        parser.addErrorListener(new ConsoleErrorListener());

        MarkerBuilder markerBuilder = parser.unit().accept(new MarkerBuilderKeyword());
        while (builder.getTokenType() != null) {
            completeActions(markerBuilder, builder, false);
            builder.advanceLexer();
        }
        completeActions(markerBuilder, builder, true);
    }

    private void completeActions(MarkerBuilder markerBuilder, PsiBuilder builder, boolean hard) {
        markerBuilder.actions.completeActions(builder, hard);
    }

    private PadaParser createParser(PsiBuilder psiBuilder) {
        ANTLRInputStream source;
        try {
            source = new ANTLRInputStream(new CharSequenceReader(psiBuilder.getOriginalText()));
        } catch (IOException e) {
            throw new IllegalStateException("source text is not available");
        }
        return new PadaParser(new BufferedTokenStream(new PadaLexer(source)));
    }
}

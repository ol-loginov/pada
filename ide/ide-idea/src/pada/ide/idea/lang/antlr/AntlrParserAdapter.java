package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.psi.stubs.PadaFileStub;

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

        PadaParser.UnitContext parserResult = parser.unit();

        MarkerBuilder[] markerBuilders = new MarkerBuilder[]{
                parserResult.accept(new MarkerBuilderAboutError()),
                parserResult.accept(new MarkerBuilderKeyword())
        };

        while (builder.getTokenType() != null) {
            completeActions(markerBuilders, builder, false);
            builder.advanceLexer();
        }
        completeActions(markerBuilders, builder, true);
    }

    private void completeActions(MarkerBuilder[] markerBuilders, PsiBuilder builder, boolean hard) {
        for (MarkerBuilder markerBuilder : markerBuilders) {
            markerBuilder.actions.completeActions(builder, hard);
        }
    }

    private PadaParser createParser(PsiBuilder psiBuilder) {
        AntlrLexerAdapter lexerAdapter = (AntlrLexerAdapter) ((PsiBuilderImpl) psiBuilder).getLexer();
        PadaLexer antlrLexer = new PadaLexer(lexerAdapter.getCharStream());
        return new PadaParser(new BufferedTokenStream(antlrLexer));
    }
}

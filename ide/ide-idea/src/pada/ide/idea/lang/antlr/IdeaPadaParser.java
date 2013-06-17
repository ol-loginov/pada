package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaBaseListener;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.LangElements;
import pada.ide.idea.lang.PadaParserDefinition;

public class IdeaPadaParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        PsiBuilder.Marker unitMarker = builder.mark();

        PadaParser parser = new PadaParser(new BufferedTokenStream(new IdeaTokenSource(builder)));
        parser.addParseListener(configure(new UnitListener(), builder));
        parser.addParseListener(configure(new UnitPackageListener(), builder));
        parser.unit();

        unitMarker.done(PadaParserDefinition.PADA_FILE);
        return builder.getTreeBuilt();
    }

    private <T extends AstListener> T configure(T listener, PsiBuilder builder) {
        listener.builder = builder;
        return listener;
    }

    static class AstListener extends PadaBaseListener {
        public PsiBuilder builder;
        public PsiBuilder.Marker marker;
    }

    static class UnitListener extends AstListener {
        @Override
        public void enterUnit(@org.antlr.v4.runtime.misc.NotNull PadaParser.UnitContext ctx) {
            marker = builder.mark();
        }

        @Override
        public void exitUnit(@org.antlr.v4.runtime.misc.NotNull PadaParser.UnitContext ctx) {
            marker.done(LangElements.unitDefinition);
        }
    }

    static class UnitPackageListener extends AstListener {
        @Override
        public void enterUnitPackage(PadaParser.UnitPackageContext ctx) {
            marker = builder.mark();
        }

        @Override
        public void exitUnitPackage(PadaParser.UnitPackageContext ctx) {
            marker.done(LangElements.packageDefinition);
        }
    }
}

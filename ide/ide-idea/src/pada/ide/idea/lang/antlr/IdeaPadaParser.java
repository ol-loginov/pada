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

public class IdeaPadaParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        PadaParser parser = new PadaParser(new BufferedTokenStream(new IdeaTokenSource(builder)));
        parser.addParseListener(new ASTBuilder(builder));
        parser.unit();

        return builder.getTreeBuilt();
    }

    static class ASTBuilder extends PadaBaseListener {
        private final PsiBuilder builder;
        private PsiBuilder.Marker marker;

        public ASTBuilder(PsiBuilder builder) {
            this.builder = builder;
        }

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

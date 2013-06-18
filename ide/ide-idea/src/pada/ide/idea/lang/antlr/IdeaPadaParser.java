package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaBaseListener;
import pada.ide.idea.lang.LangElements;
import pada.ide.idea.lang.PadaParserDefinition;

import java.util.Stack;

public class IdeaPadaParser implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        PsiBuilder.Marker unitMarker = builder.mark();

        AntlrParser parser = new AntlrParser(new BufferedTokenStream(new IdeaTokenSource(builder)));
        parser.addParseListener(new UnitListener(builder));
        parser.unit();

        unitMarker.done(PadaParserDefinition.PADA_FILE);
        return builder.getTreeBuilt();
    }

    static class AstListener extends PadaBaseListener {
    }

    static class UnitListener extends AstListener {
        public final PsiBuilder builder;
        public Stack<PsiBuilder.Marker> markerStack = new Stack<PsiBuilder.Marker>();

        UnitListener(PsiBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void enterEveryRule(@org.antlr.v4.runtime.misc.NotNull ParserRuleContext ctx) {
            markerStack.push(builder.mark());
        }

        @Override
        public void exitEveryRule(@org.antlr.v4.runtime.misc.NotNull ParserRuleContext ctx) {
            markerStack.pop().done(LangElements.instance().findByAntlrType(ctx.getRuleIndex()));
        }
    }
}

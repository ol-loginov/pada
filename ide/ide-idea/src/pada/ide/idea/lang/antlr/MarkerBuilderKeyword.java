package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import pada.ide.idea.lang.LangToken;
import pada.ide.idea.lang.psi.PadaKeyword;

public class MarkerBuilderKeyword extends MarkerBuilderAboutError {
    @Override
    public MarkerBuilder visitTerminal(@NotNull final TerminalNode node) {
        LangToken langToken = LangToken.findByAntlrType(node.getSymbol().getType());
        if (!langToken.isKeyword())
            return super.visitTerminal(node);

        Interval interval = intervalOf(node);
        debug("register keyword at " + interval);
        final MarkerHolder marker = new MarkerHolder();

        actions.add(interval.a, new MarkerAction() {
            @Override
            void run(PsiBuilder builder) {
                debug("enter keyword at " + builder.getCurrentOffset());
                marker.set(builder);
            }
        });

        super.visitTerminal(node);

        actions.add(interval.b, new MarkerAction() {
            @Override
            public void run(PsiBuilder builder) {
                debug("leave keyword at " + builder.getCurrentOffset());
                marker.collapse(PadaKeyword.typeOf(node.getText()));
            }
        });

        return defaultResult();
    }
}

package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import pada.ide.idea.lang.LangToken;
import pada.ide.idea.lang.psi.PadaKeyword;

public class MarkerBuilderKeyword extends MarkerBuilder {
    @Override
    public MarkerBuilder visitTerminal(@NotNull final TerminalNode node) {
        LangToken langToken = LangToken.findByAntlrType(node.getSymbol().getType());
        if (!langToken.isKeyword())
            return defaultResult();

        Interval interval = intervalOf(node);
        final MarkerHolder marker = new MarkerHolder();

        debug("register keyword at " + interval);
        actions.add(interval.a, new MarkerAction() {
            @Override
            void run(PsiBuilder builder) {
                debug("enter keyword at " + builder.getCurrentOffset());
                marker.set(builder);
            }
        });
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

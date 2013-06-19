package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import pada.util.ObjectHolder;

public class MarkerBuilderAboutError extends MarkerBuilder {
    @Override
    public MarkerBuilder visitErrorNode(final ErrorNode node) {
        Interval interval = intervalOf(node);
        final ObjectHolder<PsiBuilder.Marker> marker = new ObjectHolder<PsiBuilder.Marker>();

        debug("register error at " + interval);
        actions.add(interval.a, new MarkerAction() {
            @Override
            void run(PsiBuilder builder) {
                debug("enter error at " + builder.getCurrentOffset());
                marker.value = builder.mark();
            }
        });
        actions.add(interval.b, new MarkerAction() {
            @Override
            public void run(PsiBuilder builder) {
                debug("leave error at " + builder.getCurrentOffset());
                marker.value.error(node.getText());
                marker.value = null;
            }
        });
        return defaultResult();
    }

    private Interval intervalOf(TerminalNode node) {
        int a = node.getSymbol().getStartIndex();
        int b = node.getSymbol().getStopIndex();
        a = a == -1 ? Integer.MAX_VALUE : a;
        b = b == -1 ? Integer.MAX_VALUE : b;
        return (a <= b) ? new Interval(a, b) : new Interval(b, a);
    }
}

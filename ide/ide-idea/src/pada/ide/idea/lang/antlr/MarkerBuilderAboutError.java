package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;

public class MarkerBuilderAboutError extends MarkerBuilder {
    @Override
    public MarkerBuilder visitErrorNode(final ErrorNode node) {
        Interval interval = intervalOf(node);
        debug("register error at " + interval);
        final MarkerHolder marker = new MarkerHolder();

        actions.add(interval.a, new MarkerAction() {
            @Override
            void run(PsiBuilder builder) {
                debug("enter error at " + builder.getCurrentOffset());
                marker.set(builder);
            }
        });

        super.visitErrorNode(node);

        actions.add(interval.b, new MarkerAction() {
            @Override
            public void run(PsiBuilder builder) {
                debug("leave error at " + builder.getCurrentOffset());
                marker.error(node.getText());
            }
        });

        return defaultResult();
    }
}

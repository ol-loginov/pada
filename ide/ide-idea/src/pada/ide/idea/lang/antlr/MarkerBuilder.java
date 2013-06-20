package pada.ide.idea.lang.antlr;

import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Log;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public abstract class MarkerBuilder extends AbstractParseTreeVisitor<MarkerBuilder> {
    Markers actions = new Markers();

    @Override
    protected MarkerBuilder defaultResult() {
        return this;
    }

    public static void debug(String message) {
        Log.print(message);
        Log.flush();
    }

    static abstract class MarkerAction {
        int index;

        abstract void run(PsiBuilder builder);
    }

    static class Markers {
        private int actionIndex;
        private Map<Integer, List<MarkerAction>> actions = new TreeMap<Integer, List<MarkerAction>>();

        public int add(int index, MarkerAction action) {
            action.index = ++actionIndex;
            requireList(actions, index).add(action);
            return action.index;
        }

        private List<MarkerAction> requireList(Map<Integer, List<MarkerAction>> map, int index) {
            List<MarkerAction> list = map.get(index);
            if (list == null) {
                map.put(index, list = new ArrayList<MarkerAction>());
            }
            return list;
        }

        public void completeActions(PsiBuilder psiBuilder, boolean hard) {
            Iterator<Map.Entry<Integer, List<MarkerAction>>> iterator = actions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<MarkerAction>> kv = iterator.next();

                boolean psiBuilderTarget = psiBuilder.getCurrentOffset() >= kv.getKey();// || psiBuilder.rawTokenTypeStart(1) == kv.getKey();
                if (!hard && !psiBuilderTarget) {
                    break;
                }

                Collections.sort(kv.getValue(), new Comparator<MarkerAction>() {
                    @Override
                    public int compare(MarkerAction o1, MarkerAction o2) {
                        return o1.index - o2.index;
                    }
                });
                for (MarkerAction action : kv.getValue()) {
                    action.run(psiBuilder);
                }
                iterator.remove();
            }
        }
    }

    public static Interval intervalOf(TerminalNode node) {
        return intervalOf(node.getSymbol());
    }

    public static Interval intervalOf(Token symbol) {
        int a = symbol.getStartIndex();
        int b = symbol.getStopIndex();
        a = a == -1 ? Integer.MAX_VALUE : a;
        b = b == -1 ? Integer.MAX_VALUE : b;
        return (a <= b) ? new Interval(a, b) : new Interval(b, a);
    }
}

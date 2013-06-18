package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.openapi.diagnostic.Log;
import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.RuleNode;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.LangElements;
import pada.ide.idea.lang.PadaElement;
import pada.ide.idea.lang.PadaParserDefinition;

import java.util.*;

public class AntlrParserAdapter implements PsiParser {
    private static void trace(String message) {
        Log.print(message);
        Log.flush();
    }

    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        MarkerFactoryListener markerFactory = new MarkerFactoryListener(builder);

        PadaParser parser = createParser(builder);
        parser.unit().accept(markerFactory);

        PsiBuilder.Marker unitMarker = builder.mark();
        try {
            while (builder.getTokenType() != null) {
                markerFactory.actions.completeActions(false);
                builder.advanceLexer();
            }
            markerFactory.actions.completeActions(true);
        } finally {
            unitMarker.done(PadaParserDefinition.PADA_FILE);
        }

        Log.flush();
        return builder.getTreeBuilt();
    }

    private PadaParser createParser(PsiBuilder psiBuilder) {
        AntlrLexerAdapter lexerAdapter = (AntlrLexerAdapter) ((PsiBuilderImpl) psiBuilder).getLexer();
        PadaLexer antlrLexer = new PadaLexer(lexerAdapter.getCharStream());
        return new PadaParser(new BufferedTokenStream(antlrLexer));
    }

    static class PsiMarker {
        public final PsiBuilder psiBuilder;
        private int actionIndex;
        private Map<Integer, List<PsiMarkerAction>> actions = new TreeMap<Integer, List<PsiMarkerAction>>();

        PsiMarker(PsiBuilder psiBuilder) {
            this.psiBuilder = psiBuilder;
        }

        public void add(int index, PsiMarkerAction action) {
            action.index = ++actionIndex;
            requireList(actions, index).add(action);
        }

        private List<PsiMarkerAction> requireList(Map<Integer, List<PsiMarkerAction>> map, int index) {
            List<PsiMarkerAction> list = map.get(index);
            if (list == null) {
                map.put(index, list = new ArrayList<PsiMarkerAction>());
            }
            return list;
        }

        public void completeActions(boolean hard) {
            Iterator<Map.Entry<Integer, List<PsiMarkerAction>>> iterator = actions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<PsiMarkerAction>> kv = iterator.next();
                if (!hard && psiBuilder.getCurrentOffset() < kv.getKey())
                    break;
                Collections.sort(kv.getValue(), new Comparator<PsiMarkerAction>() {
                    @Override
                    public int compare(PsiMarkerAction o1, PsiMarkerAction o2) {
                        return Integer.compare(o1.index, o2.index);
                    }
                });
                for (PsiMarkerAction action : kv.getValue()) {
                    action.run(psiBuilder);
                }
                iterator.remove();
            }
        }
    }

    static class ObjectRef<T> {
        public T value;
    }

    static abstract class PsiMarkerAction {
        int index;

        abstract void run(PsiBuilder builder);
    }

    static class MarkerFactoryListener extends AbstractParseTreeVisitor<PsiMarker> {
        public PsiMarker actions;

        public MarkerFactoryListener(PsiBuilder psiBuilder) {
            actions = new PsiMarker(psiBuilder);
        }

        protected PsiMarker defaultResult() {
            return actions;
        }

        @Override
        public PsiMarker visitErrorNode(final ErrorNode node) {
            Interval interval = adjust(node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex());
            final ObjectRef<PsiBuilder.Marker> marker = new ObjectRef<PsiBuilder.Marker>();
            actions.add(interval.a, new PsiMarkerAction() {
                @Override
                void run(PsiBuilder builder) {
                    trace("enter error at " + builder.getCurrentOffset());
                    marker.value = builder.mark();
                }
            });
            actions.add(interval.b, new PsiMarkerAction() {
                @Override
                public void run(PsiBuilder builder) {
                    trace("leave error at " + builder.getCurrentOffset());
                    marker.value.error(node.getText());
                }
            });
            return defaultResult();
        }

        private Interval adjust(int a, int b) {
            a = a == -1 ? Integer.MAX_VALUE - 1 : a;
            b = b == -1 ? Integer.MAX_VALUE - 1 : b;
            return (a <= b) ? new Interval(a, b) : new Interval(b, a);
        }

        @Override
        public PsiMarker visitChildren(RuleNode node) {
            final ParserRuleContext ruleContext = (ParserRuleContext) node;
            final ObjectRef<PsiBuilder.Marker> marker = new ObjectRef<PsiBuilder.Marker>();
            final PadaElement nodePsi = LangElements.instance().findByAntlrType(ruleContext.getRuleIndex());

            Interval interval = adjust(ruleContext.getStart().getStartIndex(), ruleContext.getStop().getStopIndex());
            actions.add(interval.a, new PsiMarkerAction() {
                @Override
                void run(PsiBuilder builder) {
                    trace("enter element " + nodePsi + " at " + builder.getCurrentOffset());
                    marker.value = builder.mark();
                }
            });
            super.visitChildren(node);
            actions.add(interval.b, new PsiMarkerAction() {
                @Override
                public void run(PsiBuilder builder) {
                    trace("leave element " + nodePsi + " at " + builder.getCurrentOffset());
                    marker.value.done(nodePsi);
                }
            });

            return defaultResult();
        }
    }
}

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
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.LangElements;
import pada.ide.idea.lang.PadaElement;
import pada.ide.idea.lang.PadaParserDefinition;

import java.util.*;

public class AntlrParserAdapter implements PsiParser {
    //private static final Logger LOG = Logger.getInstance(AntlrParserAdapter.class);

    public static void debug(String message) {
        Log.print(message);
        Log.flush();
    }

    @NotNull
    @Override
    public synchronized ASTNode parse(IElementType root, final PsiBuilder builder) {
        builder.setDebugMode(true);
        debug("parse: " + builder.getOriginalText());
        debug("parse length: " + builder.getOriginalText().length());

        MarkerFactoryListener markerFactory = new MarkerFactoryListener(builder);

        PadaParser parser = createParser(builder);
        PadaParser.UnitContext parserResult = parser.unit();
        parserResult.accept(markerFactory);

        PsiBuilder.Marker unitMarker = builder.mark();
        while (builder.getTokenType() != null) {
            markerFactory.actions.completeActions(false);
            builder.advanceLexer();
        }
        markerFactory.actions.completeActions(true);
        unitMarker.done(PadaParserDefinition.PADA_FILE);

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

        public int add(int index, PsiMarkerAction action) {
            action.index = ++actionIndex;
            requireList(actions, index).add(action);
            return action.index;
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

                boolean psiBuilderTarget = psiBuilder.getCurrentOffset() >= kv.getKey() || psiBuilder.rawTokenTypeStart(1) == kv.getKey();
                if (!hard && !psiBuilderTarget) {
                    break;
                }

                Collections.sort(kv.getValue(), new Comparator<PsiMarkerAction>() {
                    @Override
                    public int compare(PsiMarkerAction o1, PsiMarkerAction o2) {
                        return o1.index - o2.index;
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

        @Override
        public String toString() {
            return value == null ? "<null>" : value.toString();
        }
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
            Interval interval = intervalOf(node);
            final ObjectRef<PsiBuilder.Marker> marker = new ObjectRef<PsiBuilder.Marker>();

            debug("register error at " + interval);
            actions.add(interval.a, new PsiMarkerAction() {
                @Override
                void run(PsiBuilder builder) {
                    debug("enter error at " + builder.getCurrentOffset());
                    marker.value = builder.mark();
                }
            });
            actions.add(interval.b, new PsiMarkerAction() {
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

        private Interval intervalOf(ParserRuleContext ruleContext) {
            int a = ruleContext.getStart().getStartIndex();
            int b = ruleContext.getStop().getStartIndex();
            if (ruleContext.getChildCount() > 0) {
                if (ruleContext.getChild(0) instanceof ParserRuleContext) {
                    a = intervalOf((ParserRuleContext) ruleContext.getChild(0)).a;
                }
                if (ruleContext.getChild(0) instanceof TerminalNode) {
                    a = intervalOf(((TerminalNode) ruleContext.getChild(0))).a;
                }

                if (ruleContext.getChild(ruleContext.getChildCount() - 1) instanceof ParserRuleContext) {
                    b = intervalOf((ParserRuleContext) ruleContext.getChild(ruleContext.getChildCount() - 1)).b;
                }
                if (ruleContext.getChild(ruleContext.getChildCount() - 1) instanceof TerminalNode) {
                    b = intervalOf((TerminalNode) ruleContext.getChild(ruleContext.getChildCount() - 1)).b;
                }
            }
            a = a == -1 ? Integer.MAX_VALUE : a;
            b = b == -1 ? Integer.MAX_VALUE : b;
            return (a <= b) ? new Interval(a, b) : new Interval(b, a);
        }

        @Override
        public PsiMarker visitChildren(RuleNode node) {
            final ParserRuleContext ruleContext = (ParserRuleContext) node;
            final ObjectRef<PsiBuilder.Marker> marker = new ObjectRef<PsiBuilder.Marker>();
            final PadaElement nodePsi = LangElements.instance().findByAntlrType(ruleContext.getRuleIndex());

            Interval interval = intervalOf(ruleContext);

            debug("register enter " + nodePsi + " at " + interval + " with index " + actions.add(interval.a, new PsiMarkerAction() {
                @Override
                void run(PsiBuilder builder) {
                    debug("mark enter (" + index + ")" + nodePsi + " at " + builder.getCurrentOffset());
                    marker.value = builder.mark();
                }
            }));

            super.visitChildren(node);

            debug("register leave " + nodePsi + " at " + interval + " with index " + actions.add(interval.b, new PsiMarkerAction() {
                @Override
                public void run(PsiBuilder builder) {
                    debug("mark leave (" + index + ")" + nodePsi + " at " + builder.getCurrentOffset());
                    marker.value.done(nodePsi);
                    marker.value = null;
                }
            }));

            return defaultResult();
        }


    }
}

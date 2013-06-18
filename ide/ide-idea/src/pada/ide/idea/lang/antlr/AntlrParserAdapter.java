package pada.ide.idea.lang.antlr;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.impl.PsiBuilderImpl;
import com.intellij.openapi.diagnostic.Log;
import com.intellij.psi.tree.IElementType;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaBaseListener;
import pada.compiler.antlr4.PadaBaseVisitor;
import pada.compiler.antlr4.PadaLexer;
import pada.compiler.antlr4.PadaParser;
import pada.ide.idea.lang.LangElements;
import pada.ide.idea.lang.PadaElement;
import pada.ide.idea.lang.PadaParserDefinition;

import java.util.*;

public class AntlrParserAdapter implements PsiParser {
    @NotNull
    @Override
    public ASTNode parse(IElementType root, final PsiBuilder builder) {
        MarkerFactoryListener markerFactory = new MarkerFactoryListener(builder);

        PadaParser parser = createParser(builder);
        parser.unit().accept(markerFactory);

        PsiBuilder.Marker unitMarker = builder.mark();
        try {
            while (builder.getTokenType() != null) {
                markerFactory.actions.runActions(builder);
                builder.advanceLexer();
            }
            markerFactory.actions.closeActions(builder);
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

    static class AstListener extends PadaBaseListener {
    }

    static class PsiMarker {
        public final PsiBuilder psiBuilder;
        private Map<Integer, List<PsiMarkerAction>> enterActions = new TreeMap<Integer, List<PsiMarkerAction>>();
        private Map<Integer, List<PsiMarkerAction>> leaveActions = new TreeMap<Integer, List<PsiMarkerAction>>();
        private int errorLevel = 0;
        private PsiBuilder.Marker errorMarker = null;

        PsiMarker(PsiBuilder psiBuilder) {
            this.psiBuilder = psiBuilder;
        }

        public void add(int start, int stop, PsiMarkerAction action) {
            requireList(enterActions, start).add(0, action);
            requireList(leaveActions, stop).add(action);
        }

        private List<PsiMarkerAction> requireList(Map<Integer, List<PsiMarkerAction>> map, int index) {
            List<PsiMarkerAction> list = map.get(index);
            if (list == null) {
                map.put(index, list = new ArrayList<PsiMarkerAction>());
            }
            return list;
        }

        private List<PsiMarkerAction> optionalList(Map<Integer, List<PsiMarkerAction>> map, int index) {
            List<PsiMarkerAction> list = map.get(index);
            return list == null ? Collections.<PsiMarkerAction>emptyList() : list;
        }

        public void runActions(PsiBuilder builder) {
            closeActions(builder);
            for (PsiMarkerAction open : optionalList(enterActions, builder.getCurrentOffset())) {
                open.enter(this);
            }
        }

        public void closeActions(PsiBuilder builder) {
            Iterator<Map.Entry<Integer, List<PsiMarkerAction>>> iterator = leaveActions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<PsiMarkerAction>> kv = iterator.next();
                if (builder.getCurrentOffset() <= kv.getKey())
                    break;
                for (PsiMarkerAction action : kv.getValue()) {
                    action.leave(this);
                }
                iterator.remove();
            }
        }

        public void enterError() {
            errorLevel++;
            if (errorLevel == 1) {
                Log.print("ERROR START AT " + psiBuilder.getCurrentOffset());
                errorMarker = psiBuilder.mark();
            }
        }

        public void leaveError() {
            errorLevel--;
            if (errorLevel == 0) {
                Log.print("ERROR END AT " + psiBuilder.getCurrentOffset());
                errorMarker.error("Incorrect syntax");
                errorMarker = null;
            }
        }

        public PsiBuilder.Marker mark(PadaElement element) {
            Log.print("MARKER START " + element + " AT " + psiBuilder.getCurrentOffset());
            return psiBuilder.mark();
        }

        public void done(PsiBuilder.Marker marker, PadaElement element) {
            if (marker == null) return;
            Log.print("MARKER END " + element + " AT " + psiBuilder.getCurrentOffset());
            marker.done(element);
        }
    }

    static interface PsiMarkerAction {
        void enter(PsiMarker psiMarker);

        void leave(PsiMarker psiMarker);
    }

    static class MarkerFactoryListener extends PadaBaseVisitor<PsiMarker> {
        public PsiMarker actions;

        public MarkerFactoryListener(PsiBuilder psiBuilder) {
            actions = new PsiMarker(psiBuilder);
        }

        @Override
        protected PsiMarker defaultResult() {
            return actions;
        }

        @Override
        public PsiMarker visitErrorNode(ErrorNode node) {
            actions.add(node.getSymbol().getStartIndex(), node.getSymbol().getStopIndex(), new PsiMarkerAction() {
                PsiBuilder.Marker marker;

                @Override
                public void enter(PsiMarker psiMarker) {
                    psiMarker.enterError();
                }

                @Override
                public void leave(PsiMarker psiMarker) {
                    psiMarker.leaveError();
                }

                @Override
                public String toString() {
                    return "ErrorNode" + (marker == null ? " (closed)" : "");
                }
            });
            return defaultResult();
        }

        @Override
        public PsiMarker visit(ParseTree tree) {
            final ParserRuleContext ruleContext = (ParserRuleContext) tree;
            actions.add(ruleContext.getStart().getStartIndex(), ruleContext.getStop().getStopIndex(), new PsiMarkerAction() {
                PsiBuilder.Marker marker;

                @Override
                public void enter(PsiMarker psiMarker) {
                    marker = psiMarker.mark(LangElements.instance().findByAntlrType(ruleContext.getRuleIndex()));
                }

                @Override
                public void leave(PsiMarker psiMarker) {
                    psiMarker.done(marker, LangElements.instance().findByAntlrType(ruleContext.getRuleIndex()));
                    marker = null;
                }

                @Override
                public String toString() {
                    return "Element " + LangElements.instance().findByAntlrType(ruleContext.getRuleIndex()).toString() + (marker == null ? " (closed)" : "");
                }
            });
            return super.visit(tree);
        }
    }
}

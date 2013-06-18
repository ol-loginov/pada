package pada.ide.idea.lang.antlr;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import pada.compiler.antlr4.PadaParser;

public class AntlrParser extends PadaParser {
    public AntlrParser(TokenStream input) {
        super(input);
    }

    @Override
    public void enterRule(@NotNull ParserRuleContext localctx, int state, int ruleIndex) {
        setState(state);
        _ctx = localctx;
        if (_parseListeners != null) triggerEnterRuleEvent();
        _ctx.start = _input.LT(1);
        if (_buildParseTrees) addContextToParseTree();
    }

    @Override
    public void exitRule() {
        super.exitRule();
    }

    @Override
    public void enterRecursionRule(ParserRuleContext localctx, int ruleIndex) {
        super.enterRecursionRule(localctx, ruleIndex);
    }

    @Override
    public void enterOuterAlt(ParserRuleContext localctx, int altNum) {
        super.enterOuterAlt(localctx, altNum);
    }
}

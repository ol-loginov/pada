package pada.ide.idea.lang;

import pada.compiler.antlr4.PadaParser;

public final class LangElements {
    private static final LangElements instance = new LangElements();

    private final PadaElement[] rules = new PadaElement[PadaParser.ruleNames.length];

    private LangElements() {
        for (int i = 0; i < PadaParser.ruleNames.length; ++i) {
            rules[i] = new PadaElement(PadaParser.ruleNames[i], i);
        }
    }

    public PadaElement findByAntlrType(int antlrType) {
        if (antlrType == -1)
            return null;
        return rules[antlrType];
    }

    public static LangElements instance() {
        return instance;
    }
}


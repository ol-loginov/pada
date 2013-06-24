package pada.ide.idea;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.lang.LangToken;
import pada.ide.idea.lang.antlr.AntlrLexerAdapter;

import java.util.HashMap;
import java.util.Map;

public class PadaLanguageHighlighter extends SyntaxHighlighterBase {
    private static final Map<IElementType, TextAttributesKey> keys1 = new HashMap<IElementType, TextAttributesKey>();

    public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey("PADA.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);

    static {
        for (LangToken token : LangToken.TOKENS) {
            if (token.isKeyword())
                keys1.put(token, KEYWORD);
        }
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new AntlrLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return pack(keys1.get(tokenType));
    }
}

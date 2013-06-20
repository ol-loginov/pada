package pada.ide.idea;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

public class PadaLanguage extends Language {
    public static final PadaLanguage INSTANCE = new PadaLanguage();
    public static final String NAME = "Pada";

    public PadaLanguage() {
        super(NAME, "text/pada");
        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
            @NotNull
            @Override
            protected SyntaxHighlighter createHighlighter() {
                return new PadaLanguageHighlighter();
            }
        });
    }
}

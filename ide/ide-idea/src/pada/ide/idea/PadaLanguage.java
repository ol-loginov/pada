package pada.ide.idea;

public class PadaLanguage extends Language {
    public static final PadaLanguage INSTANCE = new PadaLanguage();
    public static final String NAME = "Pada";

    public PropertiesLanguage() {
        super(NAME, "text/pada");
//        SyntaxHighlighterFactory.LANGUAGE_FACTORY.addExplicitExtension(this, new SingleLazyInstanceSyntaxHighlighterFactory() {
//            protected SyntaxHighlighter createHighlighter() {
//                return new PropertiesHighlighter();
//            }
//        });
    }
}

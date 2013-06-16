package pada.ide.idea.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.IStubFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import pada.ide.idea.PadaLanguage;
import pada.ide.idea.lang.psi.stubs.elements.PadaStubFileElementType;

public class PadaParserDefinition implements ParserDefinition {
    public static final IStubFileElementType PADA_FILE = new PadaStubFileElementType(PadaLanguage.INSTANCE);


    @NotNull
    public Lexer createLexer(Project project) {
        return new PadaLexer();
    }

    public PsiParser createParser(Project project) {
        return new PadaParser();
    }

    public IFileElementType getFileNodeType() {
        return PADA_FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSets.WHITE_SPACE;
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TokenSets.COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSets.STRING_LITERALS;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return PadaPsiCreator.createElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new PadaFileImpl(viewProvider);
    }
}

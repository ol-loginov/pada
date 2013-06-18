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
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.PadaLanguage;
import pada.ide.idea.lang.antlr.IdeaPadaLexer;
import pada.ide.idea.lang.antlr.IdeaPadaParser;
import pada.ide.idea.lang.psi.PadaFileImpl;
import pada.ide.idea.lang.psi.stubs.elements.PadaStubFileElementType;

public class PadaParserDefinition implements ParserDefinition {
    public static final IStubFileElementType PADA_FILE = new PadaStubFileElementType(PadaLanguage.INSTANCE);

    @NotNull
    public Lexer createLexer(Project project) {
        return new IdeaPadaLexer();
    }

    public PsiParser createParser(Project project) {
        return new IdeaPadaParser();
    }

    public IFileElementType getFileNodeType() {
        return PADA_FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(
                LangTokens.instance().findByAntlrType(PadaLexer.Space));
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TokenSet.create(
                LangTokens.instance().findByAntlrType(PadaLexer.LineComment),
                LangTokens.instance().findByAntlrType(PadaLexer.Comment));
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(
                LangTokens.instance().findByAntlrType(PadaLexer.StringLiteral),
                LangTokens.instance().findByAntlrType(PadaLexer.CharacterLiteral));
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return PadaPsiResolver.createElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new PadaFileImpl(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}

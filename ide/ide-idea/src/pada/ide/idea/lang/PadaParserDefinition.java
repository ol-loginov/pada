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
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import pada.compiler.antlr4.PadaLexer;
import pada.ide.idea.lang.antlr.AntlrLexerAdapter;
import pada.ide.idea.lang.antlr.AntlrParserAdapter;
import pada.ide.idea.lang.psi.PadaFilePsi;
import pada.ide.idea.lang.psi.stubs.PadaFileStub;

public class PadaParserDefinition implements ParserDefinition {
    @NotNull
    public Lexer createLexer(Project project) {
        return new AntlrLexerAdapter();
    }

    public PsiParser createParser(Project project) {
        return new AntlrParserAdapter();
    }

    public IFileElementType getFileNodeType() {
        return PadaFileStub.TYPE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(
                LangToken.findByAntlrType(PadaLexer.Space));
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return TokenSet.create(
                LangToken.findByAntlrType(PadaLexer.LineComment),
                LangToken.findByAntlrType(PadaLexer.Comment));
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(
                LangToken.findByAntlrType(PadaLexer.StringLiteral),
                LangToken.findByAntlrType(PadaLexer.CharacterLiteral));
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        return PadaPsiResolver.createElement(node);
    }

    public PsiFile createFile(FileViewProvider viewProvider) {
        return new PadaFilePsi(viewProvider);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}

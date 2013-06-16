package pada.ide.idea.lang;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;

public interface Tokens {
    TokenSet WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE);

    TokenSet COMMENTS = TokenSet.create(TokenType.WHITE_SPACE);
}

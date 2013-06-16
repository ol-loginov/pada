package pada.compiler.ast;

import org.antlr.v4.runtime.misc.NotNull;
import pada.compiler.antlr4.PadaBaseVisitor;
import pada.compiler.antlr4.PadaParser;

public class AstBuilder extends PadaBaseVisitor<AstBuilder> {
    private String packageName = "";

    @Override
    protected AstBuilder defaultResult() {
        return this;
    }

    @Override
    public AstBuilder visitUnitPackage(@NotNull PadaParser.UnitPackageContext ctx) {
        packageName = ctx.typeName().getText();
        return defaultResult();
    }
}

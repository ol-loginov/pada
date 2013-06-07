package pada.compiler;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import pada.compiler.antlr4.PadaBaseVisitor;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.Writer;

public class CodeTreeWriter<T extends Writer> extends PadaBaseVisitor<T> {
    private final T writer;
    private int indent = 0;

    public CodeTreeWriter(T writer) {
        this.writer = writer;
    }

    @Override
    public T visitErrorNode(@NotNull ErrorNode node) {
        writeln("");
        writeln("!!ERROR!! " + node.getText());
        return writer;
    }

    @Override
    protected T defaultResult() {
        return writer;
    }

    @Override
    public T visitTerminal(@NotNull TerminalNode node) {
        write(node.getText());
        return defaultResult();
    }

    @Override
    public T visitIdentifier(@NotNull PadaParser.IdentifierContext ctx) {
        write(ctx.getText());
        return defaultResult();
    }

    @Override
    public T visitPackageName(@NotNull PadaParser.PackageNameContext ctx) {
        write("[");
        int index = 0;
        for (PadaParser.IdentifierContext name : ctx.identifier()) {
            if (index++ > 0) write(".");
            name.Identifier().accept(this);
        }
        write("]");
        return defaultResult();

    }

    @Override
    public T visitChildren(@NotNull RuleNode node) {
        ParserRuleContext ruleContext = (ParserRuleContext) node;
        boolean indentChildren
                = node instanceof PadaParser.CompilationUnitContext
                || node instanceof PadaParser.TypeDeclContext
                || node instanceof PadaParser.ClassDeclContext
                || node instanceof PadaParser.ClassBodyContext
                || node instanceof PadaParser.TypeMemberDeclContext;

        write("(" + PadaParser.ruleNames[ruleContext.getRuleIndex()]);

        ++indent;
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChild(i) instanceof TerminalNode)
                continue;
            if (indentChildren) {
                indent();
            } else {
                write(" ");
            }
            node.getChild(i).accept(this);
        }
        --indent;
        write(")");
        return defaultResult();
    }

    private CodeTreeWriter write(String text) {
        try {
            writer.append(text);
        } catch (IOException e) {
            //silent bob
        }
        return this;
    }

    private CodeTreeWriter write(char text) {
        try {
            writer.append(text);
        } catch (IOException e) {
            //silent bob
        }
        return this;
    }

    private CodeTreeWriter flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            // silent bob again
        }
        return this;
    }

    private void indent() {
        writeln("");
        for (int i = 0; i < indent; ++i)
            write("  ");
    }

    private void writeln(String text) {
        try {
            write(text).write('\n');
        } finally {
            flush();
        }
    }
}

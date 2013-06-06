package pada.compiler;

import org.antlr.v4.runtime.misc.NotNull;
import pada.compiler.antlr4.PadaBaseVisitor;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.Writer;

public class CodeTreeWriter extends PadaBaseVisitor<Writer> {
    private final Writer writer;
    private int indent = 0;

    public CodeTreeWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public Writer visitCompilationUnit(@NotNull final PadaParser.CompilationUnitContext ctx) {
        writeScope("@unit", new Runnable() {
            @Override
            public void run() {
                ctx.packageDecl().accept(CodeTreeWriter.this);
            }
        });
        return writer;
    }

    private void writeScope(String text, Runnable scopeWriter) {
        try {
            writeln(text);
            ++indent;
            scopeWriter.run();
        } finally {
            --indent;
        }
    }

    private void writeln(String text) {
        try {
            for (int i = 0; i < indent; ++i)
                writer.append(' ');
            writer.append(text).append('\n');
        } catch (IOException e) {
            // silent bob
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                // silent bob again
            }
        }
    }
}

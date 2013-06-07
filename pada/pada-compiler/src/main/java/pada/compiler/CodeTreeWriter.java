package pada.compiler;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import pada.compiler.antlr4.PadaBaseVisitor;
import pada.compiler.antlr4.PadaParser;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

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
    public T visitCompilationUnit(@NotNull final PadaParser.CompilationUnitContext ctx) {
        writeScope("UNIT", new Runnable() {
            @Override
            public void run() {
                if (ctx.packageDecl() != null) {
                    ctx.packageDecl().accept(CodeTreeWriter.this);
                }
                for (PadaParser.ImportDeclContext importDecl : ctx.importDecl()) {
                    importDecl.accept(CodeTreeWriter.this);
                }
                for (PadaParser.TypeDeclContext importDecl : ctx.typeDecl()) {
                    importDecl.accept(CodeTreeWriter.this);
                }
            }
        });
        return defaultResult();
    }

    @Override
    public T visitPackageDecl(@NotNull PadaParser.PackageDeclContext ctx) {
        indent();
        write("PACKAGE(");
        ctx.packageName().accept(this);
        writeln(")");
        return defaultResult();
    }

    @Override
    public T visitPackageName(@NotNull PadaParser.PackageNameContext ctx) {
        for (TerminalNode identifier : ctx.Identifier()) {
            write('.');
            write(identifier.getText());
        }
        return defaultResult();
    }

    @Override
    public T visitImportDecl(@NotNull PadaParser.ImportDeclContext ctx) {
        indent();
        write("IMPORT(");
        ctx.packageName().accept(this);
        if (ctx.importAlias() != null) {
            write(" AS ");
            write(ctx.importAlias().Identifier().getText());
        }
        writeln(")");
        return defaultResult();
    }

    @Override
    public T visitClassDecl(@NotNull final PadaParser.ClassDeclContext ctx) {
        writeScope("CLASS(" + ctx.Identifier().getText() + ")", new Runnable() {
            @Override
            public void run() {
                writeAnnotations(ctx.annotation());
                writeModifiers(ctx.typeMod());
            }
        });
        return defaultResult();
    }

    @Override
    public T visitAnnotation(@NotNull PadaParser.AnnotationContext ctx) {
        indent();
        writeScope("WITH ANNOTATION(" + ctx.typeName().getText() + ")", new Runnable() {
            @Override
            public void run() {
            }
        });
        return defaultResult();
    }

    private void writeAnnotations(List<PadaParser.AnnotationContext> annotationList) {
        for (PadaParser.AnnotationContext annotation : annotationList) {
            annotation.accept(CodeTreeWriter.this);
        }
    }

    private void writeModifiers(Iterable<PadaParser.TypeModContext> typeMods) {
        for (PadaParser.TypeModContext typeMode : typeMods) {
            write("AS " + typeMode.getText());
        }
    }

    private void writeScope(String text, Runnable scopeWriter) {
        try {
            indent();
            writeln(text);
            ++indent;
            scopeWriter.run();
        } finally {
            --indent;
        }
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

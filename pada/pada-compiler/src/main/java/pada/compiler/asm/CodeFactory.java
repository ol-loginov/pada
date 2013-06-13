package pada.compiler.asm;

import org.objectweb.asm.ClassWriter;

public class CodeFactory {
    static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static ClassGena getClassGenerator() {
        return new ClassGena(new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS));
    }
}

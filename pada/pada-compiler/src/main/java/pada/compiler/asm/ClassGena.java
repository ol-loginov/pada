package pada.compiler.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class ClassGena {
    private final ClassWriter classWriter;

    public ClassGena(ClassWriter classWriter) {
        this.classWriter = classWriter;
    }

    public void beginClass() {
        classWriter.visit(Opcodes.V1_6,
                Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                "asm/visit/Counter",
                null,
                Object.class.getCanonicalName().replace('.', '/'),
                CodeFactory.EMPTY_STRING_ARRAY
        );
    }

}

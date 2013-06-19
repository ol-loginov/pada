package pada.compiler.ast.impl;

import pada.compiler.ast.PadaType;
import pada.compiler.ast.PadaUnit;

public class PadaUnitNode implements PadaUnit {
    final PadaType[] types;

    public PadaUnitNode(PadaType[] types) {
        this.types = types == null ? PadaType.EMPTY_ARRAY : types;
    }

    @Override
    public PadaType[] getTypes() {
        return types;
    }
}

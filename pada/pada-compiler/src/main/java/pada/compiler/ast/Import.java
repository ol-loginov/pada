package pada.compiler.ast;

public class Import {
    private boolean staticImport;
    private TypeName target;
    private String alias;

    public TypeName getTarget() {
        return target;
    }

    public void setTarget(TypeName target) {
        this.target = target;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isStaticImport() {
        return staticImport;
    }

    public void setStaticImport(boolean staticImport) {
        this.staticImport = staticImport;
    }
}

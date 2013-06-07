package pada.compiler;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class UnitErrorListener extends BaseErrorListener {
    private final String file;
    private final List<String> errorList;

    public UnitErrorListener(String file) {
        this.file = file;
        this.errorList = new ArrayList<>();
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public String getFile() {
        return file;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        errorList.add(String.format("%s:[%d,%d] %s", file, line, charPositionInLine, msg));
    }
}

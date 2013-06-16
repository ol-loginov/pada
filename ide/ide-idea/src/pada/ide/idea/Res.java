package pada.ide.idea;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

public class Res {
    private static Reference<ResourceBundle> ourBundle;

    protected static final String PATH_TO_BUNDLE = "messages.PadaBundle";

    private Res() {
    }

    public static String message(@PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key, Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle bundle = null;
        if (ourBundle != null) bundle = ourBundle.get();
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(PATH_TO_BUNDLE);
            ourBundle = new SoftReference<>(bundle);
        }
        return bundle;
    }
}

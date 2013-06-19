package pada.util;

public class ObjectHolder<T> {
        public T value;

        @Override
        public String toString() {
            return value == null ? "<null>" : value.toString();
        }
}

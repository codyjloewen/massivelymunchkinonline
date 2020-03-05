package mmo;

import java.util.List;
import java.util.stream.Collectors;

public final class Utility {
    public static <T> T as(final Class<T> clazz, final Object object) {
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        return null;
    }

    public static <T> List<T> ofType(final Class<T> clazz, final List<?> objects) {
        return objects.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }
}

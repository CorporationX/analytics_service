package faang.school.analytics.util;

import java.util.Arrays;
import java.util.Optional;

public class EnumUtil {

    public static <T extends Enum<T>> T getEnum(Class<T> enumClass, String name) {

        if (enumClass == null || name == null || !enumClass.isEnum()) {
            return null;
        }

        Optional<T> ans = Arrays.stream(enumClass.getEnumConstants())
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findAny();

        if (ans.isPresent()) {
            return ans.get();
        }

        int num;
        try {
            num = Integer.parseInt(name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Can't parse enum: " + e);
        }

        for (T e : enumClass.getEnumConstants()) {
            if (e.ordinal() == num) {
                return e;
            }
        }

        throw new IllegalArgumentException("Unknown enum: " + name);
    }
}

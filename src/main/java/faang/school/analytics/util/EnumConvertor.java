package faang.school.analytics.util;

import java.util.Optional;
import java.util.stream.Stream;

public class EnumConvertor {
    public static <T extends Enum<T>> T convert(Class<T> enumClass, String name) throws IllegalAccessException {
        if (name == null || !enumClass.isEnum()) {
            return null;
        }
        Stream<T> enumTypes = Stream.of(enumClass.getEnumConstants());

        Optional<T> enumType = enumTypes.filter(type -> type.name().equalsIgnoreCase(name)).findFirst();
        if (enumType.isPresent()) {
            return enumType.get();
        }

        int ord;
        ord = Integer.parseInt(name);
        for(T type : enumClass.getEnumConstants()) {
            if (ord == type.ordinal()) {
                return type;
            }
        }
        throw new IllegalAccessException("Unknown enum: " + name);
    }
}
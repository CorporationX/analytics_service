package faang.school.analytics.convertor;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public class EnumConvertor {
    public static <T extends Enum<T>> T convert(Class<T> enumClass, String name) {
        if (name == null || enumClass == null || !enumClass.isEnum()) {
            return null;
        }
        T[] enumConstants = enumClass.getEnumConstants();
        Stream<T> enumTypes = Stream.of(enumConstants);

        Optional<T> enumType = enumTypes.filter(type -> type.name().equalsIgnoreCase(name)).findFirst();
        if (enumType.isPresent()) {
            return enumType.get();
        }
        log.info("Cannot find enum by sting name {}", name);

        int ord;

        try {
            ord = Integer.parseInt(name);
        } catch (NumberFormatException e) {
            log.error("Cannot find enum {}", name);
            throw new IllegalArgumentException("Unknown enum: " + name);
        }

        if (ord >= 0 && ord < enumConstants.length) {
            return enumConstants[ord];
        } else {
            log.error("ordinal out of range");
            throw new IllegalArgumentException("Ordinal out of bounds: " + name);
        }
    }
}

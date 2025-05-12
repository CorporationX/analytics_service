package faang.school.analytics.util;

import faang.school.analytics.exception.DataValidationException;

import java.util.Arrays;
import java.util.Optional;

public class EnumConverter {
    public static <T extends Enum<T>> T fromValue(Class<T> enumClass, String value) {
        if (value == null) {
            throw new DataValidationException("Value cannot be null");
        }
        if (enumClass == null || !enumClass.isEnum()) {
            throw new DataValidationException("Invalid enum class provided");
        }

        Optional<T> enumValue = parseAsNumber(enumClass, value);

        return enumValue.orElseGet(() -> parseAsString(enumClass, value));
    }

    private static <T extends Enum<T>> Optional<T> parseAsNumber(Class<T> enumClass, String value) {
        try {
            int intValue = Integer.parseInt(value);
            return Arrays.stream(enumClass.getEnumConstants())
                    .filter(e -> e.ordinal() == intValue)
                    .findFirst();
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static <T extends Enum<T>> T parseAsString(Class<T> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value) || e.name().replace("_", " ").equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new DataValidationException(String.format("Invalid %s for value: %s.", enumClass.getSimpleName(), value)));
    }
}

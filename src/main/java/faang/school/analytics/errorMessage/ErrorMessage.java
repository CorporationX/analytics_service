package faang.school.analytics.errorMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {
    private static final String ERROR_DESERIALIZATION = "Ошибка десериализации события комментария: %s";

    public static String formatDeserializationError(Object object) {
        return String.format(ERROR_DESERIALIZATION, object);
    }
}

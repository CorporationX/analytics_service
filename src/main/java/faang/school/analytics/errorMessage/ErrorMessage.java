package faang.school.analytics.errorMessage;

public class ErrorMessage {
    private static final String ERROR_DESERIALIZATION = "Deserialization error: %s";

    public static String formatDeserializationError(Object object) {
        return String.format(ERROR_DESERIALIZATION, object);
    }
}

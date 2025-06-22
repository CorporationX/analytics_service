package faang.school.analytics.exception;

import java.io.IOException;

public class CommentEventDeserializationException extends RuntimeException {

    private static final String DESERIALIZATION_ERROR_TEMPLATE =
            "Ошибка десериализации события комментария: %s";

    public CommentEventDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentEventDeserializationException(String message) {
        super(message);
    }

    public static CommentEventDeserializationException fromIOException(IOException cause) {
        String errorDetails = cause.getClass().getSimpleName() + ": " + cause.getMessage();
        String message = String.format(DESERIALIZATION_ERROR_TEMPLATE, errorDetails);
        return new CommentEventDeserializationException(message, cause);
    }
}
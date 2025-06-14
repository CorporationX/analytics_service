package faang.school.analytics.exception;

public class CommentEventDeserializationException extends RuntimeException {

    public CommentEventDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentEventDeserializationException(String message) {
        super(message);
    }
}

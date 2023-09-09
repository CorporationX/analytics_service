package faang.school.analytics.exception;

public class MessageReadException extends RuntimeException {

    public MessageReadException(Throwable cause) {
        super(cause);
    }

    public MessageReadException(String message) {
        super(message);
    }
}

package faang.school.analytics.exception;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String messageBodyIsEmpty) {
        super(messageBodyIsEmpty);
    }
}

package faang.school.analytics.listener;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String messageBodyIsEmpty) {
        super(messageBodyIsEmpty);
    }
}

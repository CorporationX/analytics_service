package faang.school.analytics.listener;

public class EventProcessingException extends RuntimeException {
    public EventProcessingException(String message, Throwable cause) {
        super(message,cause);
    }
}

package faang.school.analytics.exeption;

public class AnalyticsEventSaveException extends RuntimeException {
    public AnalyticsEventSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

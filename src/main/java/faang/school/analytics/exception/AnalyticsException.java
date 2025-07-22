package faang.school.analytics.exception;

public class AnalyticsException  extends RuntimeException{

    public AnalyticsException() {
        super();
    }

    public AnalyticsException(String message) {
        super(message);
    }

    public AnalyticsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalyticsException(Throwable cause) {
        super(cause);
    }
}
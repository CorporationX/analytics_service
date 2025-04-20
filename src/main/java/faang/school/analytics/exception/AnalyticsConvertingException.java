package faang.school.analytics.exception;

public class AnalyticsConvertingException extends RuntimeException {

    public AnalyticsConvertingException(String message, Object... args) {
        super(String.format(message, args));
    }
}

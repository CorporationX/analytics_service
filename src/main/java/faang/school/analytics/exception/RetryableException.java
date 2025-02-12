package faang.school.analytics.exception;

public class RetryableException extends RuntimeException {
    public RetryableException(String message) {
        super(message);
    }

}

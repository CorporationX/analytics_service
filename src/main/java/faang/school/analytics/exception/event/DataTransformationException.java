package faang.school.analytics.exception.event;

public class DataTransformationException extends RuntimeException {

    public DataTransformationException(String message) {
        super(message);
    }

    public DataTransformationException(String message, Throwable cause) {
        super(message, cause);
    }
}
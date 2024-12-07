package faang.school.analytics.exception;

public class DataValidationException extends RuntimeException {

    public static final String DATA_VALIDATION_ERROR = "DataValidationException occurred: ";

    public DataValidationException() {
        super();
    }

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataValidationException(Throwable cause) {
        super(cause);
    }
}


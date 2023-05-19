package faang.school.analytics.exception;

public class DataValidationException extends BusinessException {
    public DataValidationException(String code, String message) {
        super(code, message);
    }
}

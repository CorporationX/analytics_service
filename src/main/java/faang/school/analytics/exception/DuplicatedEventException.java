package faang.school.analytics.exception;

public class DuplicatedEventException extends NonRetryableException {

    public DuplicatedEventException(String message) {
        super(message);
    }
}

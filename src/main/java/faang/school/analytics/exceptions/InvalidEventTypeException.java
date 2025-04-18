package faang.school.analytics.exceptions;

public class InvalidEventTypeException extends InvalidRequestException {
    public InvalidEventTypeException(String message) {
        super(message);
    }
}

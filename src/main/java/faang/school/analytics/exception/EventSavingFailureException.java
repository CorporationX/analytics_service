package faang.school.analytics.exception;

public class EventSavingFailureException extends RuntimeException{

    public EventSavingFailureException(String message) {
        super(message);
    }
}

package faang.school.analytics.exception;

public enum MessageError {
    EVENT_TYPE_MAPPER_NOT_FOUNT("Mapper for this event type is not found"),
    ;

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

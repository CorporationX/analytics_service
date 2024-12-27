package faang.school.analytics.exception;

import lombok.Getter;

@Getter
public class EventProcessingException extends RuntimeException {

    private static final String EVENT_PROCESSING_EXCEPTION_MESSAGE = "Event processing exception for event type: %s";

    public EventProcessingException(Class<?> eventType, Throwable cause) {
        super(String.format(EVENT_PROCESSING_EXCEPTION_MESSAGE, eventType), cause);
    }
}

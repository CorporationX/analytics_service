package faang.school.analytics.exception.event;

public class EventDeserializationException extends RuntimeException {
  public EventDeserializationException(String message, Throwable cause) {
    super(message, cause);
  }
}

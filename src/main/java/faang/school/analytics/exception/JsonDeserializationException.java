package faang.school.analytics.exception;

public class JsonDeserializationException extends RuntimeException {

    public JsonDeserializationException(String message, Object... args) {
        super(String.format(message, args));
    }
}

package faang.school.analytics.exception;

public class EntityDeserializeException extends RuntimeException {
    public EntityDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public <T> EntityDeserializeException(Class<T> valueType, String body, Throwable cause) {
        super("Cannot deserialize JSON content from given JSON content String.\n" +
                "body given " + body + "\n" +
                "body class " + valueType.getSimpleName() + "\n" +
                cause.getMessage(), cause);
    }
}

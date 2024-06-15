package faang.school.analytics.handler;

public interface EventHandler<T> {
    boolean canHandle(T event);
    void handle(T event);
}
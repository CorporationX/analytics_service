package faang.school.analytics.handler;

public interface EventHandler<T> {

    void handle(T event);

}

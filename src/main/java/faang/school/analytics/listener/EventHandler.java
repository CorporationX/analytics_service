package faang.school.analytics.listener;

public interface EventHandler <T> {
    void handle(T event);
}

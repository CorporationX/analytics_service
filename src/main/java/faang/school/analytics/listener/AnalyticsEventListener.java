package faang.school.analytics.listener;

public interface AnalyticsEventListener<T> {
    void processEvent(T event);
}

package faang.school.analytics.service;

public interface AnalyticService<T> {
    void save(T eventDto);

    boolean supportsEventType(T eventType);
}

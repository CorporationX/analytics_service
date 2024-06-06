package faang.school.analytics.service;

import faang.school.analytics.event.Event;

public interface AnalyticsService<T extends Event> {

    void save(T event);
}

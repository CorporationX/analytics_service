package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface EventService {

    void saveAnalyticsEvent(AnalyticsEvent event);
}

package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {

    void saveEvent(AnalyticsEvent entity);
}

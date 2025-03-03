package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {
    void saveRecommendationEvent(AnalyticsEvent event);
    void saveEvent(AnalyticsEvent event);
}

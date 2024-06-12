package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsService {

    void save(AnalyticsEvent event);
}

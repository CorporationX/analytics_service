package faang.school.analytics.service;

import faang.school.analytics.model.entity.AnalyticsEvent;

public interface AnalyticsEventService {

    void saveEvent(AnalyticsEvent event);
}

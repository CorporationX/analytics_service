package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {
    AnalyticsEvent saveEvent (AnalyticsEvent event);
}

package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {
    AnalyticsEventDto saveEvent(AnalyticsEvent analyticsEvent);
}

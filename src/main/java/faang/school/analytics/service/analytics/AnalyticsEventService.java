package faang.school.analytics.service.analytics;

import faang.school.analytics.dto.AnalyticsEventDto;

public interface AnalyticsEventService {

    void saveEvent(AnalyticsEventDto eventDto);
}

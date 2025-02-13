package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.AnalyticsEvent;

public interface AnalyticsEventService {
    void saveEvent(AnalyticsEvent event);

    List<AnalyticsEventDto> getAnalytics(
            long receiverId,
            EventType eventType,
            Interval interval,
            LocalDateTime from,
            LocalDateTime to
    );

}

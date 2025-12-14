package faang.school.analytics.service;

import java.time.LocalDateTime;
import java.util.List;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;

public interface AnalyticsEventService {

    AnalyticsEventDto saveEvent(CreateAnalyticsEventDto dto);

    List<AnalyticsEventDto> getAnalytics(long receiverId, EventType eventType, Interval interval, LocalDateTime from,
            LocalDateTime to);

}

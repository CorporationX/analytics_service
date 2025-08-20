package faang.school.analytics.service;

import faang.school.analytics.event.EventDto;
import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

public interface AnalyticsEventService {
    void saveEvent(EventDto eventDto);
    EventDto getAnalitics(long receiverId, EventType eventType, Interval interval,
                          LocalDateTime from, LocalDateTime to);
}

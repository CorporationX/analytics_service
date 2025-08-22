package faang.school.analytics.service;

import faang.school.analytics.dto.RequestAnalyticsDto;
import faang.school.analytics.dto.EventDto;

import java.util.List;

public interface AnalyticsEventService {
    void saveEvent(EventDto eventDto);
    List<EventDto> getAnalitics(RequestAnalyticsDto requestAnalyticsDto);
}

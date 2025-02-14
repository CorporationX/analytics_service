package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.AnalyticsEventRequestDto;
import faang.school.analytics.model.AnalyticsEvent;

import java.util.List;

public interface AnalyticsEventService {
    void saveEvent(AnalyticsEventDto event);

    List<AnalyticsEventDto> getAnalytics(AnalyticsEventRequestDto analyticsEventRequestDto);
}

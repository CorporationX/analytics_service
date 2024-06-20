package faang.school.analytics.service;

import faang.school.analytics.dto.event.AnalyticsEventDto;
import faang.school.analytics.model.EventType;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsEventService {
    AnalyticsEventDto save(AnalyticsEventDto analyticsEventDto);
    List<AnalyticsEventDto> getAnalytics(Long receiverId, EventType eventType, String intervalType, Long intervalQuantity, LocalDate from, LocalDate to);
}
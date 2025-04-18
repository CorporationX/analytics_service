package faang.school.analytics.service;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsEventService {
    List<AnalyticsEvent> getAnalytics(Long receiverId, EventType eventType, LocalDateTime start, LocalDateTime end);
}

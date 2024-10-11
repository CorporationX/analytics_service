package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

public interface AnalyticsEventDto {
    EventType getEventType();
    LocalDateTime getReceivedAt();
}

package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

public record AnalyticsEventResponseDto(long actorId, long receiverId, EventType eventType, LocalDateTime receivedAt) {
}

package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

public record AnalyticsEventDto(
        long id,
        long receiverId,
        long actorId,
        LocalDateTime receivedAt,
        EventType eventType
) {}

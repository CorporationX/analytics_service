package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto (
    Long id,
    long receiverId,
    long actorId,
    EventType eventType,
    LocalDateTime receivedAt
) {
}

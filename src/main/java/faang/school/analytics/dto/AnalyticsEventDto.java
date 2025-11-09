package faang.school.analytics.dto;

import java.time.LocalDateTime;

import faang.school.analytics.model.EventType;
import lombok.Builder;

@Builder
public record AnalyticsEventDto (
    long id,
    long receiverId,
    long actorId,
    EventType eventType,
    LocalDateTime receivedAt
) {
}

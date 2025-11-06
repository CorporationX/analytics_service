package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        long id,
        long receiverId,
        long actorId,
        EventType eventType,
        LocalDateTime receivedAt
) {
}

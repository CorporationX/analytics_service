package faang.school.analytics.dto.event;

import faang.school.analytics.model.EventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseAnalyticsEventDto(
        Long id,
        Long receiverId,
        Long actorId,
        EventType eventType,
        LocalDateTime receivedAt
) {
}

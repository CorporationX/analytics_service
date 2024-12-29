package faang.school.analytics.model.dto;

import java.time.LocalDateTime;

public record AnalyticsEventDto(
        long id,
        long receiverId,
        long actorId,
        String eventType,
        LocalDateTime receivedAt
) {
}

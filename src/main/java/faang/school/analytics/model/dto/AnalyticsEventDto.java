package faang.school.analytics.model.dto;

import java.time.LocalDateTime;

public record AnalyticsEventDto(
        long receiverId,
        long actorId,
        String eventType,
        LocalDateTime receivedAt
) {
}

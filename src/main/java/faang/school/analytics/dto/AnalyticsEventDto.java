package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public record AnalyticsEventDto(
        long id,
        long receiverId,
        long actorId,
        EventType eventType,
        LocalDateTime receivedAt
) {
}

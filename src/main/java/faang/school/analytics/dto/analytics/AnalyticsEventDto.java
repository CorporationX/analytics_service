package faang.school.analytics.dto.analytics;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        Long id,
        @NonNull Long receiverId,
        @NonNull Long actorId,
        @NonNull EventType eventType,
        LocalDateTime receivedAt
) {
}

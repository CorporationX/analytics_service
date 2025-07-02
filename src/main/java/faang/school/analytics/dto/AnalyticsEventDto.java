package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        long receiverId,
        long actorId,
        LocalDateTime receivedAt
) {
}

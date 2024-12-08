package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        Long id,
        @Positive long receiverId,
        @Positive long actorId,
        @NotNull EventType eventType,
        @NotNull LocalDateTime receivedAt
) {
}

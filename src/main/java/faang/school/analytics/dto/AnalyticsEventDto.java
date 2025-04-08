package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(

        @NotNull
        Long receiverId,

        @NotNull
        Long actorId,

        @NotNull
        EventType eventType,

        @NotNull
        LocalDateTime receivedAt
) {
}

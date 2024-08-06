package faang.school.analytics.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventDto(
        Long id,
        @NotNull Long receiverId,
        @NotNull Long actorId,
        @NotBlank(message = "Event type should not be empty.") String eventType,
        @NotNull LocalDateTime receivedAt
) {}
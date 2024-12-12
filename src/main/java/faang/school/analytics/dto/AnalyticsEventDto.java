package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AnalyticsEventDto(
        @NotNull long id,
        @NotNull long receiverId,
        @NotNull long actorId,
        @NotNull EventTypeDto eventType,
        @NotNull LocalDateTime receivedAt
) {

}

package faang.school.analytics.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AnalyticsCreateEventDto(
        @NotNull(message = "receiverId is null")
        @Min(1)
        long receiverId,
        @NotNull(message = "actorId is null")
        @Min(1)
        long actorId,
        @NotNull(message = "eventType id null")
        EventTypeDto eventType
) {

}

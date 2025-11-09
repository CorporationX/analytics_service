package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAnalyticsEventDto (
    @NotNull
    long receiverId,

    @NotNull
    long actorId,

    @NotNull
    EventType eventType
) {
}

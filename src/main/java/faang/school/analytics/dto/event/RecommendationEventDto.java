package faang.school.analytics.dto.event;

import faang.school.analytics.dto.EventTypeDto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RecommendationEventDto(
        @NotNull Long receiverId,
        @NotNull Long authorId,
        @NotNull LocalDateTime createdAt,
        @NotNull EventTypeDto eventType
) implements Event {

}

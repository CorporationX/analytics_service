package faang.school.analytics.dto.event;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record RecommendationEventDto(
        @NotNull Long recommendationId,
        @NotNull Long receiverId,
        @NotNull Long authorId,
        @NotNull LocalDateTime createdAt
) implements Event {

}

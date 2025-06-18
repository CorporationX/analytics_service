package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record RecommendationEventDto(
        Long recommendationId,
        Long authorId,
        Long receiverId,
        LocalDateTime createdAt) {
}

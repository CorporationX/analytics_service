package faang.school.analytics.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record RecommendationEvent(
        long id,
        long authorId,
        long receiverId,
        LocalDateTime createdAt) {
        }

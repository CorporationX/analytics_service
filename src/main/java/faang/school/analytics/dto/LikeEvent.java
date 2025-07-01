package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikeEvent(
        Long postId,
        Long authorId,
        Long userId,
        LocalDateTime createdAt
) {
}

package faang.school.analytics.dto.event.likeEvent;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostLikeEvent(
    Long postId,
    Long authorId,
    Long userId,
    LocalDateTime timestamp
) {
}

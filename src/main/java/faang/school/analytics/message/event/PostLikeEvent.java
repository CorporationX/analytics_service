package faang.school.analytics.message.event;

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

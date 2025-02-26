package faang.school.analytics.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentEvent(
        Long postAuthorId,
        Long commentAuthorId,
        Long postId,
        Long commentId,
        LocalDateTime commentedAt
) {
}

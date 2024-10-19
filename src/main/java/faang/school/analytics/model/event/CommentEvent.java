package faang.school.analytics.model.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentEvent(
        long authorId,
        long commentId,
        LocalDateTime commentedAt
) {
}

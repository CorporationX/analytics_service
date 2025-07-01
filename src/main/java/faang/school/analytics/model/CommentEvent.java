package faang.school.analytics.model;

import java.time.LocalDateTime;

public record CommentEvent(
        Long commentId,
        Long postId,
        Long authorId,
        LocalDateTime createdAt
){}
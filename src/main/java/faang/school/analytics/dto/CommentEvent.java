package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record CommentEvent(long commentId, long authorId, long postId, LocalDateTime date) {
}

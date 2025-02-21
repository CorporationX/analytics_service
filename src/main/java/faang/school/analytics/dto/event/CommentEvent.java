package faang.school.analytics.dto.event;

import java.time.LocalDateTime;

public record CommentEvent(long postId, long authorId, long commentId, LocalDateTime date) {
}

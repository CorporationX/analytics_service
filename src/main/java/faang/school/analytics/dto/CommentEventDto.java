package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record CommentEventDto(long postId, long actorId, long commentId, long postAuthorId, LocalDateTime createdAt) {
}

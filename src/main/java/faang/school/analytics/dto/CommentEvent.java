package faang.school.analytics.dto;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CommentEvent(
        long postAuthorId,
        long commentAuthorId,
        LocalDateTime createdAt) {
}

package faang.school.analytics.dto.event.comment;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CommentEvent(UUID eventId,
                           long postId,
                           long commentId,
                           LocalDateTime receivedAt) {

}

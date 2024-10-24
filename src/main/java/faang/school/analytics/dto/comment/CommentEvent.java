package faang.school.analytics.dto.comment;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private Long postId;
    private Long authorId;
    private Long commentId;
    private final EventType eventType = EventType.POST_COMMENT;
    private LocalDateTime timestamp;
}

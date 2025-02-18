package faang.school.analytics.event.comment;

import faang.school.analytics.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEvent extends BaseEvent {
    private Long commentId;
    private String comment;
    private Long userId;
    private Long postId;
    private CommentEventType eventType;
    private LocalDateTime createdAt;
}

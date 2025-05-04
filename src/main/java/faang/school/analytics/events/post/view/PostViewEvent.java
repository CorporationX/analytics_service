package faang.school.analytics.events.post.view;

import faang.school.analytics.events.Event;
import faang.school.analytics.model.post.PostEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEvent implements Event {
    private Long postId;
    private Long authorId;
    private Long viewerId;
    private LocalDateTime timestamp;
    private PostEventType postEventType;
}

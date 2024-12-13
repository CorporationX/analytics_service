package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentEvent {
    private long postId;
    private long authorId;
    private long commentId;
    private LocalDateTime createdAt;
}

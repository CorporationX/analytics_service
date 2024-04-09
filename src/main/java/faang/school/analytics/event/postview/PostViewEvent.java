package faang.school.analytics.event.postview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEvent {
    private long postId;
    private long authorId;
    private long userId;
    private LocalDateTime viewedAt;
}

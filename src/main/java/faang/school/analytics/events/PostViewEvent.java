package faang.school.analytics.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class PostViewEvent {
    private Long postId;
    private Long userId;
    private Long authorId;
    private LocalDateTime viewedAt;
}

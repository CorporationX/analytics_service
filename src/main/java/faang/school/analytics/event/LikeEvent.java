package faang.school.analytics.event;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeEvent {
    private Long postId;
    private Long authorId;
    private Long likedByUserId;
    private LocalDateTime timestamp;
}


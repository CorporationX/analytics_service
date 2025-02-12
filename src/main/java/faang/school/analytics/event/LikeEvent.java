package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeEvent {

    private Long postId;
    private Long authorId;
    private Long userId;
    private LocalDateTime likeTime;
}

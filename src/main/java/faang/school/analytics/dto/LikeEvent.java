package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEvent {
    private Long postId;
    private Long authorPostId;
    private Long likingUserId;
    private Long userId;
    private Long likedUserId;
    private LocalDateTime createdAt;
}

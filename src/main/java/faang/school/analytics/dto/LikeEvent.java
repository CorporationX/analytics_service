package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeEvent {
    private Long postId;
    private Long authorPostId;
    private Long userId;
    private LocalDateTime createdAt;
}

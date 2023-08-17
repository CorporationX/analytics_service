package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikeEventDto {
    private Long userId;
    private Long postId;
    private Long authorId;
    private LocalDateTime createdAt;
}

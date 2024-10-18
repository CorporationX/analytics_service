package faang.school.analytics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LikeEventDto {
    private Long postId;
    private Long authorId;
    private Long userId;
    private LocalDateTime timestamp;
}
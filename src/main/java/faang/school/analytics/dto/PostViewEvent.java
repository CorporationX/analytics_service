package faang.school.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostViewEvent {
    private Long postId;
    private Long authorId;
    private Long viewerId;
    private LocalDateTime viewTime;
}

package faang.school.analytics.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEventDto {
    private Long postId;
    private Long authorId;
    private Long viewerId;
    private LocalDateTime viewedAt;
}

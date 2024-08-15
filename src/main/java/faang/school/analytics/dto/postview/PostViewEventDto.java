package faang.school.analytics.dto.postview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewEventDto {
    private long postId;
    private long authorId;
    private long userId;
    private LocalDateTime timestamp;
}

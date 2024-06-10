package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostViewEvent {
    private long postId;
    private long authorId;
    private long userId;
    private LocalDateTime viewedAt;
}

package faang.school.analytics.dto.event;

import faang.school.analytics.until.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEvent {
    private long postId;
    private long authorId;
    private long userId;
    private LocalDateTime likedAt;
    private EventType type;
}

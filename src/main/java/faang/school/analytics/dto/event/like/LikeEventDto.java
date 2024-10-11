package faang.school.analytics.dto.event.like;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
public class LikeEventDto {
    private long authorId;
    private long likerId;
    private EventType eventType;
    private LocalDateTime createdAt;
}

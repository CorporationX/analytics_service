package faang.school.analytics.model.dto;

import faang.school.analytics.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostViewEvent {
    private EventType eventType = EventType.POST_VIEW;
    private Long postId;
    private Long authorId;
    private Long actorId;
    private LocalDateTime receivedAt;
}

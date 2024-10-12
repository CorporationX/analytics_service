package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostViewEventDto {
    private long postId;
    private long receiverId;
    private long actorId;
    private final EventType eventType = EventType.POST_VIEW;
    private LocalDateTime receivedAt;
}

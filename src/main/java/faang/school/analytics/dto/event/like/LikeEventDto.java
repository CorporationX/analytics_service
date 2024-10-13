package faang.school.analytics.dto.event.like;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeEventDto {
    private Long postAuthorId;
    private Long likerId;
    private EventType eventType;
    private LocalDateTime createdAt;
}

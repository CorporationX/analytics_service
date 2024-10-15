package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class AnalyticsEventDto extends AbstractEventDto {
    private Long id;
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

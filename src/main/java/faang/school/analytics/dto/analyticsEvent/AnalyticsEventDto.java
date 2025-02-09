package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsEventDto {
    private Long id;
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AnalyticsEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}
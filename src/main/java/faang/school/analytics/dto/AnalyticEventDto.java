package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

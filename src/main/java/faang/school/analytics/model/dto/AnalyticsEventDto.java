package faang.school.analytics.model.dto;

import faang.school.analytics.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnalyticsEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}


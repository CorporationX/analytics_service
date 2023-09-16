package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class AnalyticResponseDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private ZonedDateTime receivedAt;
}
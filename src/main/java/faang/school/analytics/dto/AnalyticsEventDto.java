package faang.school.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private String eventType;
    private LocalDateTime receivedAt;
}

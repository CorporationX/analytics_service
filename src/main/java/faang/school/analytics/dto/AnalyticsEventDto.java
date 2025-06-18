package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalyticsEventDto {
    private long id;

    private long receiverId;

    private long actorId;

    private String eventType;

    private LocalDateTime receivedAt;
}

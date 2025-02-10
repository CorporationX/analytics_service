package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;

import java.time.LocalDateTime;

public class AnalyticsEventDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

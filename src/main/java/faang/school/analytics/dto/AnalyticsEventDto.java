package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {
    private Long id;
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

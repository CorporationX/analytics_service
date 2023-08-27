package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsEventDto {
    private Long actorId;
    private Long requesterId;
    private Long receiverId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}
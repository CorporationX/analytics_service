package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticsDto {
    private Long id;
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

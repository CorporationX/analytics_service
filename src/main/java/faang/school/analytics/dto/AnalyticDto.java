package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticDto {
    private Long id;
    private EventType eventType;
    private Long receiverId;
    private Long actorId;
    private LocalDateTime receivedAt;
}

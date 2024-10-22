package faang.school.analytics.model.dto;

import faang.school.analytics.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundRaisedEvent {
    private EventType eventType = EventType.PROJECT_DONATE;
    private Long projectId;
    private Long actorId;
    private Long amount;
    private LocalDateTime receivedAt;
}

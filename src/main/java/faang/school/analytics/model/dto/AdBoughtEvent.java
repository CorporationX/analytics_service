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
public class AdBoughtEvent {
    private final EventType eventType = EventType.ADV_BOUGHT;
    private Long postId;
    private Long userId;
    private Long amount;
    private Integer advDuration;
    private LocalDateTime receivedAt;
}

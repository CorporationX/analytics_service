package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Evgenii Malkov
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnalyticsEventDto {
    private Long id;
    private Long receiverId;
    private Long actorId;
    private EventType eventType;
    private LocalDateTime receivedAt;
}

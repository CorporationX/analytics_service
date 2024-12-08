package faang.school.analytics.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecommendationEvent {
    private Long id;

    private Long actorId;

    private Long receiverId;

    private LocalDateTime receivedAt;
}

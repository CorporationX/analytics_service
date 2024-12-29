package faang.school.analytics.domain.dto.events.recommendation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecommendationEvent {
    private Long id;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;
}

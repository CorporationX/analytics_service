package faang.school.analytics.dto.recommendation;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
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

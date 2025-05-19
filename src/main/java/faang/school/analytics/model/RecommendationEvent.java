package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationEvent {
    Long recommendationId;
    Long authorId;
    Long receiverId;
    LocalDateTime createdAt;
}

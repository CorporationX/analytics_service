package faang.school.analytics.dto.analyticsEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationAnalyticDto {
    private Long recommendationId;
    private Long authorId;
    private Long receivedId;
    private LocalDateTime receivedAt;
}

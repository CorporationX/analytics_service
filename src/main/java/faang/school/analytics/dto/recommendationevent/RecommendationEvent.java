package faang.school.analytics.dto.recommendationevent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationEvent implements Serializable {
    private Long recommendationId;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;
}

package faang.school.analytics.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationEvent {
    private Long recommendationId;
    private Long authorId;
    private Long receiverId;
    private LocalDateTime createdAt;
}


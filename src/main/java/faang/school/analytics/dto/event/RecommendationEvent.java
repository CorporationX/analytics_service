package faang.school.analytics.dto.event;

import lombok.Data;

@Data
public class RecommendationEvent {
    private Long authorId;
    private Long receiverId;
}

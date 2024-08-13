package faang.school.analytics.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecommendationEvent {

    private Long id;
    private Long receiverId;
    private Long authorId;
    private LocalDateTime receivedAt;
}

package faang.school.analytics.model;

import java.time.LocalDateTime;

public class RecommendationEvent {

    private Long id;
    private Long receiverId;
    private Long authorId;
    private LocalDateTime receivedAt;
}

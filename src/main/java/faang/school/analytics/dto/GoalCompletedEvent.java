package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record GoalCompletedEvent(
        long userId,
        long goalId,
        LocalDateTime completedAt
) {
}

package faang.school.analytics.event;

import java.time.LocalDateTime;

public record GoalCompletedEvent(
        long userId,
        long goalId,
        LocalDateTime completedAt
) {
}

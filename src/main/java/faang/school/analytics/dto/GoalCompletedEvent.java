package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record GoalCompletedEvent(
        Long userId,
        Long goalId,
        LocalDateTime completedAt
) {
}

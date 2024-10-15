package faang.school.analytics.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GoalCompletedEvent(
        long userId,
        long goalId,
        LocalDateTime completedAt
) {
}

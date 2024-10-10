package faang.school.analytics.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GoalCompletedEventDto(
        long userId,
        long goalId,
        LocalDateTime completedAt
) {
}

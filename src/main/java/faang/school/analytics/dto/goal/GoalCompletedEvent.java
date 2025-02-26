package faang.school.analytics.dto.goal;

import java.time.LocalDateTime;

public record GoalCompletedEvent(Long userId, Long goalId, LocalDateTime date) {
}
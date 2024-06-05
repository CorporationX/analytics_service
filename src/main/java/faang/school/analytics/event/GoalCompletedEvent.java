package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class GoalCompletedEvent {
    private Long userId;
    private Long goalId;
    private LocalDateTime completedAt;
}

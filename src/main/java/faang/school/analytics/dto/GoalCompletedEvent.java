package faang.school.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalCompletedEvent {
    private int userId;
    private int goalId;
    private LocalDateTime completedAt;
}

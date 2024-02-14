package faang.school.analytics.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class GoalCompletedEvent {
    private long goalId;
    private long userId;
    private LocalDateTime timestamp;
}

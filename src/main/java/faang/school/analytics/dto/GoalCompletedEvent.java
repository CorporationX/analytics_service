package faang.school.analytics.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class GoalCompletedEvent {
    private long goalId;
    private long userId;
    private LocalDateTime timestamp;
}

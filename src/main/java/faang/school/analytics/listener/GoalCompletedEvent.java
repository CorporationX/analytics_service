package faang.school.analytics.listener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalCompletedEvent {

    private Long actorId;

    private Long goalId;

    private String eventType;

    private LocalDateTime receivedAt;
}
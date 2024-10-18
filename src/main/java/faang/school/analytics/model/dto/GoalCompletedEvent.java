package faang.school.analytics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalCompletedEvent {
    private Long receiverId;
    private Long actorId;
}
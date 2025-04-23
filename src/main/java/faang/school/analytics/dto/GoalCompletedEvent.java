package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record GoalCompletedEvent(

        Long userId,
        Long goalId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime completedAt
) {
}

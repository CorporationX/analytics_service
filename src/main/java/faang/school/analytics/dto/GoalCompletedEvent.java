package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoalCompletedEvent(
        @JsonProperty("userId") Long userId,
        @JsonProperty("goalId") Long goalId
) {
}

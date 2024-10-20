package faang.school.analytics.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisTopics {
    PROFILE_VIEW("profile-view"),
    SEARCH_APPEARANCE("search-appearance"),
    LIKE_EVENT("like_channel"),
    GOAL_COMPLETED("goal-completed");

    private final String topic;
}

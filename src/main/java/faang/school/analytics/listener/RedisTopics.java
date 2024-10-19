package faang.school.analytics.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RedisTopics {
    PROFILE_VIEW("profile-view"),
    SEARCH_APPEARANCE("search-appearance");

    private final String topic;
}

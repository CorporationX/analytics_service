package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventType {
    PROFILE_VIEW(0.5),
    PROJECT_VIEW(0.5),
    FOLLOWER(1.0),
    POST_PUBLISHED(0.7),
    POST_VIEW(0.5),
    POST_LIKE(0.8),
    POST_COMMENT(0.6),
    SKILL_RECEIVED(1.0),
    RECOMMENDATION_RECEIVED(0.6),
    ADDED_TO_FAVOURITES(0.6),
    PROJECT_INVITE(0.7),
    TASK_COMPLETED(0.5),
    GOAL_COMPLETED(0.7),
    ACHIEVEMENT_RECEIVED(0.5),
    PROFILE_APPEARED_IN_SEARCH(0.3),
    PROJECT_APPEARED_IN_SEARCH(0.4);

    private final double weight;

    public static EventType of(int type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    public static double getWeightByName(String name) {
        for (EventType eventType : EventType.values()) {
            if (eventType.name().equals(name)) {
                return eventType.getWeight();
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + name);
    }

    public static double getMaximumRating() {
        return Arrays.stream(EventType.values())
                .mapToDouble(EventType::getWeight)
                .sum();
    }
}

package faang.school.analytics.model;

import java.util.Arrays;

public enum EventType {
    PROFILE_VIEW,
    PROJECT_VIEW,
    FOLLOWER,
    POST_PUBLISHED,
    POST_VIEW,
    POST_LIKE,
    POST_COMMENT,
    SKILL_RECEIVED,
    RECOMMENDATION_RECEIVED,
    ADDED_TO_FAVOURITES,
    PROJECT_INVITE,
    TASK_COMPLETED,
    GOAL_COMPLETED,
    ACHIEVEMENT_RECEIVED,
    PROFILE_APPEARED_IN_SEARCH,
    PROJECT_APPEARED_IN_SEARCH;

    public static EventType of(int type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    public static EventType getFromString(String type) {

        if (type == null) {
            return null;
        }

        boolean canParse = Arrays.stream(values()).anyMatch(s -> s.name().equalsIgnoreCase(type));

        if (canParse) {
            return EventType.valueOf(type);
        } else {
            try {
                return of(Integer.parseInt(type));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unknown event type: " + type);
            }
        }
    }
}

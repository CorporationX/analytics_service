package faang.school.analytics.model;

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
    PROJECT_APPEARED_IN_SEARCH,
    PREMIUM_BOUGHT;

    public static EventType of(String type) {
        if (isInteger(type)) {
            for (EventType eventType : EventType.values()) {
                if (eventType.ordinal() == Integer.parseInt(type)) {
                    return eventType;
                }
            }
        } else {
            for (EventType eventType : EventType.values()) {
                if (eventType.name().equalsIgnoreCase(type)) {
                    return eventType;
                }
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    private static boolean isInteger(String type) {
        return type.matches("\\d+");
    }
}

package faang.school.analytics.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public static EventType getType(String eventType) {
        if (isOnlyDigits(eventType)) {
            return byIndex(Integer.parseInt(eventType));
        } else return byString(eventType);
    }

    private static EventType byIndex(int eventIndex) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == eventIndex) {
                return eventType;
            }
        }
        log.info("No such event type index : {}", eventIndex);
        throw new IllegalArgumentException("No such event type index: " + eventIndex);
    }

    private static EventType byString(String type) {
        if (type != null) {
            for (EventType eventType : EventType.values()) {
                if (type.equalsIgnoreCase(eventType.toString())) {
                    return eventType;
                }
            }
        }
        log.info("Unknown event type: {}", type);
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    private static boolean isOnlyDigits(String eventType) {
        boolean isDigit = true;
        for (int i = 0; i < eventType.length() && isDigit; i++) {
            if (!Character.isDigit(eventType.charAt(i))) {
                isDigit = false;
            }
        }
        return isDigit;
    }
}

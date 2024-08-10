package faang.school.analytics.model;

import faang.school.analytics.exception.ExceptionMessages;
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

    public static EventType of(int type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    public static EventType conversionToEventType(String eventString) {
        try {
            return EventType.valueOf(eventString.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                int ordinal = Integer.parseInt(eventString);
                return EventType.values()[ordinal];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e2) {
                log.error(ExceptionMessages.INVALID_INPUT_IS_SUPPLIED, e2);
                throw new IllegalArgumentException(ExceptionMessages.INVALID_INPUT_IS_SUPPLIED, e2);
            }
        }
    }
}
package faang.school.analytics.model;

import faang.school.analytics.dto.RecommendationEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<Class<?>, EventType> EVENT_TYPE_MAP = new HashMap<>();

    static {
        EVENT_TYPE_MAP.put(RecommendationEvent.class , EventType.RECOMMENDATION_RECEIVED);
    }

    public static EventType of(int type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    public static EventType getEventTypeForClass(Class<?> eventClass) {
        EventType eventType = EVENT_TYPE_MAP.get(eventClass);
        if (eventType == null) {
            log.warn("Неизвестный event-класс {}", eventClass);
            throw new IllegalArgumentException("Неизвестный event-класс: " + eventClass);
        }
        return eventType;
    }
}

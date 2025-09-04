package faang.school.analytics.model;

import faang.school.analytics.dto.RecommendationEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum EventType {
    PROFILE_VIEW(null),
    PROJECT_VIEW(null),
    FOLLOWER(null),
    POST_PUBLISHED(null),
    POST_VIEW(null),
    POST_LIKE(null),
    POST_COMMENT(null),
    SKILL_RECEIVED(null),
    RECOMMENDATION_RECEIVED(RecommendationEvent.class),
    ADDED_TO_FAVOURITES(null),
    PROJECT_INVITE(null),
    TASK_COMPLETED(null),
    GOAL_COMPLETED(null),
    ACHIEVEMENT_RECEIVED(null),
    PROFILE_APPEARED_IN_SEARCH(null),
    PROJECT_APPEARED_IN_SEARCH(null);

    private final Class<?> linkedClass;

    EventType(Class<?> eventClass) {
        this.linkedClass = eventClass;
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
        for (EventType eventType : EventType.values()) {
            if (eventType.linkedClass == eventClass) {
                return eventType;
            }
        }
        log.warn("Неизвестный event-класс {}", eventClass);
        throw new IllegalArgumentException("Неизвестный event-класс: " + eventClass);
    }
}

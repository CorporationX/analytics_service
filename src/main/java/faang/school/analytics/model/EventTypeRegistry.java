package faang.school.analytics.model;

import faang.school.analytics.dto.RecommendationEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс для ассоциации JAVA_КЛАСС: ТИП_ИВЕНТА
 *
 * @author Linempy
 * @since 20.08.2025
 */
@Slf4j
public class EventTypeRegistry {
    private static final Map<Class<?>, EventType> EVENT_TYPE_MAP = new HashMap<>();

    static {
        EVENT_TYPE_MAP.put(RecommendationEvent.class , EventType.RECOMMENDATION_RECEIVED);
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
package faang.school.analytics.model;

import faang.school.analytics.messaging.events.ProfileViewEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    PROFILE_VIEW("profileViewEvent", ProfileViewEvent.class);   //разкомментируй требуемый и допиши параметры для конструктора
//    PROJECT_VIEW,
//    FOLLOWER,
//    POST_PUBLISHED,
//    POST_VIEW,
//    POST_LIKE,
//    POST_COMMENT,
//    SKILL_RECEIVED,
//    RECOMMENDATION_RECEIVED,
//    ADDED_TO_FAVOURITES,
//    PROJECT_INVITE,
//    TASK_COMPLETED,
//    GOAL_COMPLETED,
//    ACHIEVEMENT_RECEIVED,
//    PROFILE_APPEARED_IN_SEARCH,
//    PROJECT_APPEARED_IN_SEARCH;

    private final String jsonType;
    private final Class myClass;

    public static EventType of(int type) {
        for (EventType eventType : values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }

    public static EventType get(String jsonType){
        for(EventType type : values()){
            if(type.getJsonType().equals(jsonType)){
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + jsonType);
    }
}

package faang.school.analytics.model.event.type;

import faang.school.analytics.model.event.type.service.post.like.PostLikeEvent;

public enum EventType {
    //PROFILE_VIEW(Object.class),
    //PROJECT_VIEW(Object.class),
    FOLLOWER(Object.class),// <- у кого будет задание по подписке, реализуйте класс
    //POST_PUBLISHED(Object.class),
    //POST_VIEW(Object.class),
    POST_LIKE(PostLikeEvent.class);
    //POST_COMMENT(Object.class),
    //SKILL_RECEIVED(Object.class),
    //RECOMMENDATION_RECEIVED(Object.class),
    //ADDED_TO_FAVOURITES(Object.class),
    //PROJECT_INVITE(Object.class),
    //TASK_COMPLETED(Object.class),
    //GOAL_COMPLETED(Object.class),
    //ACHIEVEMENT_RECEIVED(Object.class),
    //PROFILE_APPEARED_IN_SEARCH(Object.class),
    //PROJECT_APPEARED_IN_SEARCH(Object.class);

    private final Class<?> eventClass;

    EventType(Class<?> eventClass) {
        this.eventClass = eventClass;
    }

    public static EventType fromEvent(Object event) {
        for (EventType eventType : EventType.values()) {
            if (eventType.eventClass.equals(event.getClass())) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event class: " + event.getClass().getName());
    }
}

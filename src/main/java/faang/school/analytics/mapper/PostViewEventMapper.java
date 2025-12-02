package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PostViewEventMapper {
    public static AnalyticsEvent toAnalyticsEvent(PostViewEvent event) {
        long authorId;
        if (event.author() != null && event.author().id() != null) {
            authorId = event.author().id();
        } else {
            throw new IllegalArgumentException("Author ID is null in PostViewEvent");
        }

        return AnalyticsEvent.builder()
                .actorId(authorId)
                .receiverId(event.viewerId())
                .eventType(EventType.POST_VIEW)
                .postId(event.postId())
                .receivedAt(event.currentTime())
                .build();
    }
}

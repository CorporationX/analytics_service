package faang.school.analytics.mapper;

import faang.school.analytics.dto.PostViewEvent;
import faang.school.analytics.dto.ProfileViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class PostViewEventMapper {
    public static AnalyticsEvent toAnalyticsEvent(PostViewEvent event) {
        return AnalyticsEvent.builder()
                .actorId(event.viewerId())
                .receiverId(event.authorId())
                .postId(event.postId())
                .eventType(EventType.POST_VIEW)
                .receivedAt(LocalDateTime.now())
                .build();
    }
}

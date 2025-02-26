package faang.school.analytics.mapper;

import faang.school.analytics.dto.event.PostViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.stereotype.Component;

@Component
public class PostViewEventMapper {

    public AnalyticsEvent toEntity(PostViewEvent dto) {
        return AnalyticsEvent.builder()
                .actorId(dto.getUserId())
                .receiverId(dto.getAuthorId())
                .eventType(EventType.POST_VIEW)
                .receivedAt(dto.getViewedAt())
                .build();
    }
}
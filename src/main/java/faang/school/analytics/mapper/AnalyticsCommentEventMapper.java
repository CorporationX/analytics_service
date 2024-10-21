package faang.school.analytics.mapper;

import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnalyticsCommentEventMapper {

    public AnalyticsEvent toAnalyticsEvent(CommentEventDto commentEventDto) {
        return AnalyticsEvent.builder()
                .receiverId(commentEventDto.getPostAuthorId())
                .actorId(commentEventDto.getCommentAuthorId())
                .eventType(EventType.POST_COMMENT)
                .receivedAt(commentEventDto.getReceivedAt())
                .build();
    }
}

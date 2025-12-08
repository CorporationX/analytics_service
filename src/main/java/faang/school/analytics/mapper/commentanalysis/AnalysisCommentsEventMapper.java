package faang.school.analytics.mapper.commentanalysis;

import faang.school.analytics.dto.commentanalysis.AnalysisCommentsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

public class AnalysisCommentsEventMapper {
    public static AnalyticsEvent toAnalyticsEvent(AnalysisCommentsEventDto eventDto) {
        return AnalyticsEvent.builder()
                .receiverId(eventDto.receiverId())
                .actorId(eventDto.authorId())
                .postId(eventDto.postId())
                .commentId(eventDto.commentId())
                .eventType(EventType.POST_COMMENT)
                .receivedAt(eventDto.createdAt())
                .build();
    }
}
package faang.school.analytics.mapper.commentanalysis;

import faang.school.analytics.dto.commentanalysis.AnalysisCommentsEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;

public class AnalysisCommentsEventMapper {
    public static AnalyticsEvent toAnalyticsEvent(AnalysisCommentsEventDto eventDto) {
        return AnalyticsEvent.builder()
                .postId(eventDto.postId())
                .actorId(eventDto.authorId())
                .receiverId(eventDto.commentId()) // Здесь идентификатор комментария
                .eventType(EventType.POST_COMMENT) // Используем тип POST_COMMENT
                .receivedAt(eventDto.createdAt())
                .build();
    }
}
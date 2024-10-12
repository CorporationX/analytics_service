package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.dto.RecommendationEventDto;
import faang.school.analytics.model.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecommendationEventListener extends AbstractRedisListener<RecommendationEventDto> {

    public RecommendationEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(RecommendationEventDto.class, message, this::convertToAnalyticsEvent);
    }

    private AnalyticsEvent convertToAnalyticsEvent(RecommendationEventDto recommendationEventDto) {
        return AnalyticsEvent.builder()
                .receiverId(recommendationEventDto.getReceiverId())
                .actorId(recommendationEventDto.getAuthorId())
                .eventType(EventType.RECOMMENDATION_RECEIVED)
                .receivedAt(recommendationEventDto.getTimestamp())
                .build();
    }
}
package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.exception.DataTransformationException;
import faang.school.analytics.exception.ExceptionMessages;
import faang.school.analytics.mapper.analyticsEvent.RecommendationEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.RecommendationEvent;
import faang.school.analytics.service.analyticsEvent.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final RecommendationEventMapper recommendationEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message: {}", message.getBody());
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            AnalyticsEvent analyticsEvent = recommendationEventMapper.toEntity(recommendationEvent);
            analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
            analyticsEventService.saveEvent(analyticsEvent);
        } catch (Exception e) {
            log.error(ExceptionMessages.INVALID_TRANSFORMATION, e);
            throw new DataTransformationException(ExceptionMessages.INVALID_TRANSFORMATION, e);
        }
    }
}

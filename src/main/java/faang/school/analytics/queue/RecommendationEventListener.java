package faang.school.analytics.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.recommendationevent.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.mapRecommendationToAnalyticsEvent(event);
            analyticsEventService.saveRecommendationEvent(analyticsEvent);
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
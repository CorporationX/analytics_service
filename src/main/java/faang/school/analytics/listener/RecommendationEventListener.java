package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;

    @Qualifier("redisObjectMapper") // Resolved correctly now
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            log.info("Consumed RecommendationEvent: {}", event);
            analyticsEventService.processRecommendationEvent(event);
        } catch (Exception e) {
            log.error("Failed to process RecommendationEvent: {}", e.getMessage(), e);
        }
    }
}

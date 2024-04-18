package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.mapper.RecommendationEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;
    private final RecommendationEventMapper recommendationEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            log.info("Get recommendationEvent from recommendation topic");
            AnalyticsEvent analyticEvent = recommendationEventMapper.toAnalyticEvent(recommendationEvent);
            recommendationEventMapper.setEventType(analyticEvent);
            log.info("Transfer recommendationEvent to AnalyticsEvent");
            analyticsService.saveEvent(analyticEvent);
            log.info("Success work analyticService");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("work done");
    }
}

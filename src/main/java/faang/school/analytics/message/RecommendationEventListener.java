package faang.school.analytics.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.RecommendationEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
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
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final RecommendationEventMapper recommendationEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEventDto recommendationEventDto = objectMapper.readValue(message.getBody(),
                    RecommendationEventDto.class);
            AnalyticsEventDto analyticsEventDto = recommendationEventMapper.toAnalyticsEvent(recommendationEventDto);
            analyticsEventDto.setEventType(EventType.RECOMMENDATION_RECEIVED);
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Received message from channel {}: {}", message.getChannel(), message.getBody());
    }
}

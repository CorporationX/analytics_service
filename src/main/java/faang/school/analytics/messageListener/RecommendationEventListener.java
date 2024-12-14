package faang.school.analytics.messageListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.mapper.recommendation.RecommendationMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RecommendationMapper recommendationMapper;
    private final AnalyticsEventService analyticsEventService;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent recommendationEvent = objectMapper
                    .readValue(message.getBody(), RecommendationEvent.class);
            log.info(objectMapper.writeValueAsString(recommendationEvent));

            AnalyticsEvent analyticsEvent = recommendationMapper.toAnalyticsEvent(recommendationEvent);
            analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);

            analyticsEventService.addNewEvent(analyticsEvent);

        } catch (IOException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}

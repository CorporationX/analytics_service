package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.recommendation.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
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
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent event = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            AnalyticsEventDto eventDto = mapper.recommendationToAnalyticsDto(event);
            analyticsEventService.saveEvent(eventDto);
        } catch (IOException e) {
            log.error("Error reading value");
            throw new RuntimeException(e);
        }

    }
}

package faang.school.analytics.listener;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import faang.school.analytics.dto.CreateAnalyticsEventDto;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.exception.MessageProcessingException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper eventMapper;
    private final AnalyticsEventService analytivsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RecommendationEvent recommendationEvent = objectMapper.readValue(message.getBody(), RecommendationEvent.class);
            CreateAnalyticsEventDto analyticsEvent = eventMapper.toDto(recommendationEvent);

            log.info("Received recommendation event: {}", recommendationEvent);

            analytivsEventService.saveEvent(analyticsEvent);

        } catch (IOException e) {
            log.error("Error creating recommendation event", e);
            throw new MessageProcessingException("Failed to process incoming message", e);
        }
    }
}

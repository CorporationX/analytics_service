package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationRequestEventDto;
import faang.school.analytics.exception.DeserializeJSONException;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RecommendationEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationRequestEventDto event;
        try {
            event = objectMapper.readValue(message.getBody(), RecommendationRequestEventDto.class);
        } catch (IOException e) {
            throw new DeserializeJSONException("Could not deserialize event");
        }
        AnalyticsEvent analyticsEvent = analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventService.create(analyticsEvent);
    }
}
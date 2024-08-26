package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.PostViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
@Slf4j
@Component
public class PostViewEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {

        String channel = new String(message.getChannel());
        String jsonBody = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("Received message on channel {}: {}", channel, jsonBody);

        try {
            PostViewEventDto postViewEvent = objectMapper.readValue(jsonBody, PostViewEventDto.class);
            AnalyticsEvent newEvent = analyticsEventMapper.postViewEventToEntity(postViewEvent);
            newEvent.setEventType(EventType.POST_VIEW);
            analyticsEventService.save(newEvent);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON message: {}", jsonBody, e);
            throw new RuntimeException(e);
        }


    }
}

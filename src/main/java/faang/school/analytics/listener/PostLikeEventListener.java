package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.events.PostLikeEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostLikeEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper mapper;
    private final AnalyticsEventService service;

    @Override
    @SneakyThrows
    public void onMessage(Message message, byte[] pattern) {
        log.debug("Pattern: {}", new String(pattern, StandardCharsets.UTF_8));
        log.info("Message received: {}", message.toString());
        log.debug("Channel: {}", new String(message.getChannel(), StandardCharsets.UTF_8));
        log.debug("Body: {}", new String(message.getBody(), StandardCharsets.UTF_8));

        PostLikeEvent postLikeEvent = objectMapper.readValue(message.getBody(), PostLikeEvent.class);

        AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(postLikeEvent);
        analyticsEvent.setEventType(EventType.POST_LIKE);
        service.saveEvent(analyticsEvent);
    }
}

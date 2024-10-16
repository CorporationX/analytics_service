package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.event.ProfileVeiwEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileViewEventListener implements MessageListener {
    private final AnalyticsEventMapper mapper;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("New message received: {}", message);
        ProfileVeiwEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), ProfileVeiwEvent.class);
        } catch (IOException e) {
            log.error("Error while parsing message");
            throw new RuntimeException(e);
        }
        AnalyticsEvent analyticsEvent = mapper.toEntity(event, EventType.PROFILE_VIEW.name());
        analyticsEventService.saveEvent(analyticsEvent);
    }
}

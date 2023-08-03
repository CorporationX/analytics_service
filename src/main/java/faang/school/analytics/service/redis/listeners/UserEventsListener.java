package faang.school.analytics.service.redis.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.UserEventsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import faang.school.analytics.service.redis.events.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserEventsListener implements MessageListener {
    private final AnalyticsService analyticsService;

    private final UserEventsMapper userEventsMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEvent userEvent = null;
        try {
            userEvent = objectMapper.readValue(message.getBody(), UserEvent.class);
            AnalyticsEvent analyticsEvent = userEventsMapper.toAnalyticsEvent(userEvent);

            analyticsService.create(analyticsEvent);

            log.info("Received message: " + "User with id: " + analyticsEvent.getId() + " was " + analyticsEvent.getEventType());
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}

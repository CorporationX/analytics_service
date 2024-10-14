package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
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
public class MentorshipRequestListener implements MessageListener {

    private final ObjectMapper mapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        log.info("Received message body: {}", body);
        try {
            MentorshipRequestEvent event = mapper.readValue(message.getBody(), MentorshipRequestEvent.class);
            analyticsEventService.saveAnalyticEvent(event);
        } catch (IOException e) {
            log.error("Error saving analytics", e);
            throw new RuntimeException(e);
        }
        log.info("Received message from channel {}: {}", channel, body);
    }
}

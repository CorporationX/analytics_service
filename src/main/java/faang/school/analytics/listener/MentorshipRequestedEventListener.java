package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorshipRequestedEventListener implements MessageListener, RedisChannelEventListeners {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestedEvent event = objectMapper.readValue(message.getBody(), MentorshipRequestedEvent.class);
            log.info("Received MentorshipRequestedEvent: {}", event);
            analyticsEventService.saveEvent(analyticsEventMapper.toMentorshipEntity(event));
        } catch (IOException e) {
            log.error("Failed to parse MentorshipRequestedEvent", e);
        }
    }

    @Override
    public String getChannel() {
        return "mentorship_request";
    }
}

package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.MentorshipRequestEvent;
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
@RequiredArgsConstructor
@Slf4j
public class MentorshipRequestedEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestEvent mentorshipRequestEvent = objectMapper.readValue(message.getBody(), MentorshipRequestEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEventMentorshipRequest(mentorshipRequestEvent);
            analyticsEvent.setEventType(EventType.MENTORSHIP_REQUEST);
            analyticsEventService.saveEvent(analyticsEvent);
            log.info("MentorshipRequestEvent save");
        } catch (JsonProcessingException ex) {
            log.error("Error parsing message body to MentorshipRequestEvent: {}", ex.getMessage(), ex);
            throw new IllegalArgumentException("Invalid message format for MentorshipRequestEvent.", ex);
        } catch (IOException ex) {
            log.error("IO error occurred while processing MentorshipRequestEvent: {}", ex.getMessage(), ex);
            throw new RuntimeException("IO error occurred while processing mentorship request event.", ex);
        } catch (Exception ex) {
            log.error("Unexpected error while processing MentorshipRequestEvent: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected error while processing mentorship request event.", ex);
        }
    }
}

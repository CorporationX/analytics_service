package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytics_event.AnalyticsEventDto;
import faang.school.analytics.dto.message.MentorshipRequestMessage;
import faang.school.analytics.mapper.MentorshipRequestMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
@Slf4j
public class MentorshipRequestEventListener implements MessageListener {

    private static final EventType EVENT_TYPE = EventType.REQUEST_MENTORSHIP;

    private final ObjectMapper objectMapper;
    private final MentorshipRequestMapper mentorshipRequestMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MentorshipRequestMessage mentorshipRequestMessage = objectMapper.readValue(message.getBody(),
                    MentorshipRequestMessage.class);
            log.debug("the message has been deserialized. MentorshipRequestMessage: {}", mentorshipRequestMessage);

            AnalyticsEvent analyticsEvent = mentorshipRequestMapper.toAnalyticsEvent(mentorshipRequestMessage);
            analyticsEvent.setEventType(EVENT_TYPE);
            AnalyticsEventDto analyticsEventDto = analyticsEventService.saveEvent(analyticsEvent);
            log.debug("AnalyticsEvent save DB. AnalyticsEventDto: {}", analyticsEventDto);
        } catch (IOException e) {
            log.error("Error processing message from topic {}, message: {}", message.getChannel(), new String(message.getBody(), StandardCharsets.UTF_8), e);
            throw new RuntimeException(e);
        }
    }
}

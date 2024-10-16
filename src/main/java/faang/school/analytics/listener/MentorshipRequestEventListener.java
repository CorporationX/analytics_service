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

    private final ObjectMapper objectMapper;
    private MentorshipRequestMapper mentorshipRequestMapper;
    private AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String test = new String(message.getBody(), StandardCharsets.UTF_8).trim();
            log.info("A new message has been received from topic {}, message {}", message.getChannel(), test);

            MentorshipRequestMessage mentorshipRequestMessage = objectMapper.readValue(test,
                    MentorshipRequestMessage.class);
            log.info("the message has been deserialized. MentorshipRequestMessage: {}", mentorshipRequestMessage);

            AnalyticsEvent analyticsEvent = mentorshipRequestMapper.toAnalyticsEvent(mentorshipRequestMessage);
            log.info("MentorshipRequestMessage is mapped to AnalyticsEvent. AnalyticsEvent: {}", analyticsEvent);

            analyticsEvent.setEventType(EventType.of(16));
            log.info("AnalyticsEvent added filed EventType. AnalyticsEvent: {}", analyticsEvent);

            AnalyticsEventDto analyticsEventDto = analyticsEventService.saveEvent(analyticsEvent);
            log.info("AnalyticsEvent save DB. AnalyticsEventDto: {}", analyticsEventDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
